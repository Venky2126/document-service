package com.ama.app.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@Service
public class DynamicSwaggerToWordService {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final YAMLMapper yamlMapper = new YAMLMapper();

    public File generateWordDocFromYaml(String yamlPath) throws Exception {
        // Determine if input is YAML or JSON
        JsonNode root;
        try (FileInputStream fis = new FileInputStream(yamlPath)) {
            if (yamlPath.toLowerCase().endsWith(".yaml") || yamlPath.toLowerCase().endsWith(".yml")) {
                root = yamlMapper.readTree(fis);
            } else {
                root = jsonMapper.readTree(fis);
            }
        }

        XWPFDocument doc = new XWPFDocument();

        // Basic Info Section
        addTitle(doc, root.path("info").path("title").asText(""));
        addParagraph(doc, "Description: " + root.path("info").path("description").asText(""));
        addParagraph(doc, "Version: " + root.path("info").path("version").asText(""));

        // Paths
        if (root.has("paths")) {
            addTitle(doc, "API Endpoints");
            Iterator<Map.Entry<String, JsonNode>> paths = root.get("paths").fields();
            while (paths.hasNext()) {
                Map.Entry<String, JsonNode> entry = paths.next();
                String path = entry.getKey();
                JsonNode methods = entry.getValue();
                Iterator<Map.Entry<String, JsonNode>> ops = methods.fields();
                while (ops.hasNext()) {
                    Map.Entry<String, JsonNode> opEntry = ops.next();
                    String method = opEntry.getKey().toUpperCase();
                    JsonNode op = opEntry.getValue();

                    // API Basic Info Table
                    addSubtitle(doc, "API Details");
                    XWPFTable apiTable = doc.createTable(3, 2);
                    setTableBorders(apiTable);
                    setTableRow(apiTable.getRow(0), "API URL", path);
                    setTableRow(apiTable.getRow(1), "HTTP Method", method);
                    setTableRow(apiTable.getRow(2), "Content Type", "application/json");
                    addBlankParagraph(doc);

                    // Headers Table
                    if (op.has("parameters")) {
                        addSubtitle(doc, "Headers");
                        XWPFTable headersTable = doc.createTable(1, 4);
                        setTableBorders(headersTable);
                        setTableHeader(headersTable.getRow(0), 
                            new String[]{"Name", "Description", "Type", "Required"});

                        for (JsonNode param : op.get("parameters")) {
                            if ("header".equals(param.path("in").asText())) {
                                addTableRow(headersTable, new String[]{
                                    param.path("name").asText(""),
                                    param.path("description").asText(""),
                                    getTypeFromSchema(param.path("schema")),
                                    param.path("required").asBoolean(false) ? "Yes" : "No"
                                });
                            }
                        }
                        addBlankParagraph(doc);
                    }

                    // Detailed Request Body Table
                    if (op.has("requestBody")) {
                        addSubtitle(doc, "Request Parameters");
                        JsonNode content = op.path("requestBody").path("content");
                        if (content.has("application/json")) {
                            JsonNode schema = content.path("application/json").path("schema");
                            if (schema.has("$ref")) {
                                schema = resolveRef(root, schema.get("$ref").asText());
                            }
                            if (schema != null && schema.has("properties")) {
                                XWPFTable requestTable = doc.createTable(1, 5);
                                setTableBorders(requestTable);
                                setTableHeader(requestTable.getRow(0), 
                                    new String[]{"Field", "Description", "Type", "Required", "Constraints"});

                                addPropertiesToTable(requestTable, schema, "");
                            }
                        }
                        addBlankParagraph(doc);
                    }

                    // Detailed Response Tables
                    if (op.has("responses")) {
                        addSubtitle(doc, "Response Details");
                        Iterator<Map.Entry<String, JsonNode>> respFields = op.path("responses").fields();
                        while (respFields.hasNext()) {
                            Map.Entry<String, JsonNode> respEntry = respFields.next();
                            String code = respEntry.getKey();
                            JsonNode resp = respEntry.getValue();
                            
                            addParagraph(doc, "Status Code: " + code + " - " + resp.path("description").asText(""));
                            
                            if (resp.has("content") && resp.path("content").has("application/json")) {
                                JsonNode respSchema = resp.path("content").path("application/json").path("schema");
                                if (respSchema.has("$ref")) {
                                    respSchema = resolveRef(root, respSchema.get("$ref").asText());
                                }
                                if (respSchema != null && respSchema.has("properties")) {
                                    XWPFTable responseTable = doc.createTable(1, 5);
                                    setTableBorders(responseTable);
                                    setTableHeader(responseTable.getRow(0), 
                                        new String[]{"Field", "Description", "Type", "Required", "Constraints"});
                                    
                                    addPropertiesToTable(responseTable, respSchema, "");
                                }
                            }
                            addBlankParagraph(doc);
                        }
                    }
                }
            }
        }

        // Save file
        File out = File.createTempFile("APIDocumentation_", ".docx");
        try (FileOutputStream fos = new FileOutputStream(out)) {
            doc.write(fos);
        }
        return out;
    }

    private void addPropertiesToTable(XWPFTable table, JsonNode schema, String prefix) {
        Iterator<Map.Entry<String, JsonNode>> props = schema.path("properties").fields();
        while (props.hasNext()) {
            Map.Entry<String, JsonNode> propEntry = props.next();
            String propName = propEntry.getKey();
            JsonNode prop = propEntry.getValue();
            String fullName = prefix.isEmpty() ? propName : prefix + "." + propName;

            // Handle nested objects
            if (prop.has("properties")) {
                addPropertiesToTable(table, prop, fullName);
                continue;
            }

            // Handle arrays
            if ("array".equals(prop.path("type").asText()) && prop.has("items")) {
                JsonNode items = prop.path("items");
                if (items.has("$ref")) {
                    items = resolveRef(schema, items.get("$ref").asText());
                }
                if (items != null && items.has("properties")) {
                    addPropertiesToTable(table, items, fullName + "[]");
                    continue;
                }
            }

            // Build constraints string
            StringBuilder constraints = new StringBuilder();
            addConstraint(constraints, "maxLength", prop);
            addConstraint(constraints, "minLength", prop);
            addConstraint(constraints, "pattern", prop);
            addConstraint(constraints, "minimum", prop);
            addConstraint(constraints, "maximum", prop);
            addConstraint(constraints, "enum", prop);

            // Add row to table
            addTableRow(table, new String[]{
                fullName,
                prop.path("description").asText(""),
                getTypeFromSchema(prop),
                isRequired(schema, propName) ? "Yes" : "No",
                constraints.toString()
            });
        }
    }

    private void addConstraint(StringBuilder constraints, String constraintName, JsonNode prop) {
        if (prop.has(constraintName)) {
            if (constraints.length() > 0) {
                constraints.append(", ");
            }
            constraints.append(constraintName).append(": ");
            if (prop.get(constraintName).isArray()) {
                constraints.append(prop.get(constraintName).toString());
            } else {
                constraints.append(prop.get(constraintName).asText());
            }
        }
    }

    private JsonNode resolveRef(JsonNode root, String ref) {
        String[] parts = ref.split("/");
        JsonNode node = root;
        for (String part : parts) {
            if (!part.equals("#")) {
                node = node.path(part);
            }
        }
        return node;
    }

    private String getTypeFromSchema(JsonNode schema) {
        if (schema == null) return "Object";
        if (schema.has("type")) return schema.path("type").asText();
        if (schema.has("$ref")) return "Object";
        return "Object";
    }

    private boolean isRequired(JsonNode schema, String propName) {
        if (!schema.has("required")) return false;
        for (JsonNode req : schema.path("required")) {
            if (req.asText().equals(propName)) return true;
        }
        return false;
    }

    // Helper methods for table formatting
    private void setTableBorders(XWPFTable table) {
        table.setCellMargins(100, 100, 100, 100);
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.SINGLE);
        borders.addNewLeft().setVal(STBorder.SINGLE);
        borders.addNewRight().setVal(STBorder.SINGLE);
        borders.addNewTop().setVal(STBorder.SINGLE);
        borders.addNewInsideH().setVal(STBorder.SINGLE);
        borders.addNewInsideV().setVal(STBorder.SINGLE);
    }

    private void setTableHeader(XWPFTableRow row, String[] values) {
        for (int i = 0; i < values.length; i++) {
            XWPFTableCell cell = row.getCell(i);
            if (cell == null) cell = row.addNewTableCell();
            cell.setText(values[i]);
            cell.setColor("D3D3D3"); // Light gray background
            for (XWPFParagraph p : cell.getParagraphs()) {
                for (XWPFRun r : p.getRuns()) {
                    r.setBold(true);
                }
            }
        }
    }

    private void setTableRow(XWPFTableRow row, String key, String value) {
        row.getCell(0).setText(key);
        row.getCell(1).setText(value);
    }

    private void addTableRow(XWPFTable table, String[] values) {
        XWPFTableRow row = table.createRow();
        for (int i = 0; i < values.length; i++) {
            row.getCell(i).setText(values[i]);
        }
    }

    // Helper methods for document formatting
    private void addTitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setStyle("Heading1");
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setBold(true);
        run.setFontSize(18);
        run.setFontFamily("Arial");
    }

    private void addSubtitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setStyle("Heading2");
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setBold(true);
        run.setFontSize(14);
        run.setFontFamily("Arial");
    }

    private void addParagraph(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setFontSize(11);
        run.setFontFamily("Arial");
    }

    private void addBlankParagraph(XWPFDocument doc) {
        doc.createParagraph();
    }
}