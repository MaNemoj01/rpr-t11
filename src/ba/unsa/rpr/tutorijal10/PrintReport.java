package ba.unsa.rpr.tutorijal10;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class PrintReport extends JFrame {

    public void showReport(Connection conn) throws JRException {
        String reportSrcFile = getClass().getResource("/reports/TutorijalReport.jrxml").getFile();
        String reportsDir = getClass().getResource("/reports/").getFile();

        JasperDesign jasperDesign = JRXmlLoader.load(reportSrcFile);
        // Fields for resources path
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("reportsDirPath", reportsDir);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(500, 500);
        this.setVisible(true);
    }

    public void showReportDrzava(Connection conn,Drzava drzava) throws JRException {
        String reportSrcFile = getClass().getResource("/reports/JednostavnaDrzava.jrxml").getFile();
        String reportsDir = getClass().getResource("/reports/").getFile();
        String param = String.valueOf( drzava.getId() );
        JasperDesign jasperDesign = JRXmlLoader.load(reportSrcFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("reportsDirPath", reportsDir);
        parameters.put("param",param);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(500, 500);
        this.setVisible(true);
    }

    public void save( String fileName, Connection c ) throws JRException, IOException {
        String reportSrcFile = getClass().getResource("/reports/TutorijalReport.jrxml").getFile();
        String reportsDir = getClass().getResource("/reports/").getFile();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for resources path
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("reportsDirPath", reportsDir);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, c );
        File f = new File( fileName );
        OutputStream outputStream = new FileOutputStream(f);
        if ( fileName.contains(".docx") ) {
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(f));
            exporter.exportReport();
        }
        if ( fileName.contains(".pdf") )
            JasperExportManager.exportReportToPdfStream(print, outputStream);
        if ( fileName.contains(".xslx") ) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(f));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }
        outputStream.close();
    }

}

