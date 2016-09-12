
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nishat
 */
public class Stat_Jframe extends javax.swing.JFrame {

    private Connection connection = null;
    private ResultSet resultSet = null;
    private PreparedStatement pst = null;

    /**
     * Creates new form Stat_Jframe
     */
    public Stat_Jframe() {
        initComponents();
        init();
        connection = javaConnect.connectDb();
        currentDate();
        updateStudentDataTable();
        updateTableIdentify();
        updateDocTable();
        updateStatTable();
    }

    private void init() {
        setTitle("Statistics Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public final void currentDate() {
        //static date and time
        Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        menuDate.setText("Current Date:" + day + "/" + (month + 1) + "/" + year);
        menuDate.setForeground(Color.blue);

        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);
        menuTime.setText("Current Time:" + hour + ":" + minute + ":" + second);
        menuTime.setForeground(Color.red);


        /*//dynamic date and time
         Thread threadClock = new Thread() {
         @Override
         public void run() {
         for (;;) {
         //System.out.println("p");
         Calendar cal = new GregorianCalendar();
         int day = cal.get(Calendar.DAY_OF_MONTH);
         int month = cal.get(Calendar.MONTH);
         int year = cal.get(Calendar.YEAR);
         menuDate.setText("Current Date:" + day + "/" + (month + 1) + "/" + year);
         menuDate.setForeground(Color.blue);

         int second = cal.get(Calendar.SECOND);
         int minute = cal.get(Calendar.MINUTE);
         int hour = cal.get(Calendar.HOUR);
         menuTime.setText("Current Time:" + hour + ":" + minute + ":" + second);
         menuTime.setForeground(Color.red);
         try {
         sleep(1000);
         } catch (InterruptedException ex) {
         Logger.getLogger(AddUser_Jframe.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }
         };
         threadClock.start();*/
    }

    public void close() {
        WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
    }

    private void updateStudentDataTable() {
        try {
            String sql = "select Student_id,First_name,Last_name,Department,"
                    + "Series,Age,Height,Weight,Gender,Blood from Student_data";
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            tableStudentData.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }

    private void updateTableIdentify() {
        try {
            String sql = "select Student_id,First_name from Student_data";
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            tableIdentify.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }

    private void getValue() {
        try {
            String add1 = resultSet.getString("Student_id");
            txtStId.setText(add1);
            String add2 = resultSet.getString("First_name");
            txtStFName.setText(add2);
            String add3 = resultSet.getString("Last_name");
            txtStLName.setText(add3);
            String add4 = resultSet.getString("Department");
            txtStDept.setText(add4);
            String add5 = resultSet.getString("Series");
            txtStContact.setText(add5);
            String add6 = resultSet.getString("Age");
            txtStAge.setText(add6);
            String add7 = resultSet.getString("Height");
            txtStHeight.setText(add7);
            String add8 = resultSet.getString("Weight");
            txtStWeight.setText(add8);
            String add9 = resultSet.getString("Gender");
            comboGender.setSelectedItem(add9);
            String add10 = resultSet.getString("Blood");
            txtStBlood.setText(add10);

            byte[] imageData = resultSet.getBytes("Image");
            format = new ImageIcon(ScaledImage(imageData, labelImage.getWidth(), labelImage.getHeight()));
            labelImage.setIcon(format);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

    }

    private Image ScaledImage(byte[] img, int w, int h) {
        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        try {
            Graphics2D g2 = resizedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // convert byte array back to BufferedImage
            ByteArrayInputStream in = new ByteArrayInputStream(img);
            BufferedImage bImageFromConvert = ImageIO.read(in);

            g2.drawImage(bImageFromConvert, 0, 0, w, h, null);
            g2.dispose();
        } catch (IOException ex) {
            Logger.getLogger(Stat_Jframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resizedImage;
    }

    private void showSliderData() {
        sliderAge.setValue((int) Float.parseFloat(txtStAge.getText()));
        sliderHeight.setValue((int) Float.parseFloat(txtStHeight.getText()));
        sliderWeight.setValue((int) Float.parseFloat(txtStWeight.getText()));
    }

    private void getChartData() {
        try {
            labelChartAge.setText(resultSet.getString("Age"));
            labelChartHeight.setText(resultSet.getString("height"));
            labelChartWeight.setText(resultSet.getString("Weight"));

        } catch (SQLException ex) {
            Logger.getLogger(Stat_Jframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //save the image of selected component
    private static void saveScreenShot(Component component, String fileName) throws Exception {
        BufferedImage img = getScreenShot(component);
        ImageIO.write(img, "png", new File(fileName));
    }

    //get screenshot of selected component
    private static BufferedImage getScreenShot(Component component) {
        BufferedImage image = new BufferedImage(component.getWidth(),
                component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        return image;
    }

    private void updateDocTable() {
        try {
            String sql = "select * from Document_table";
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            tableDocument.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }

    private void getDocData() throws ParseException {
        try {
            txtDocDocId.setText(resultSet.getString("Doc_id"));
            txtDocStudentId.setText(resultSet.getString("Student_id"));
            txtDocDocName.setText(resultSet.getString("Doc_name"));
            txtDocAttach1.setText(resultSet.getString("Path"));
        } catch (SQLException ex) {
            Logger.getLogger(Stat_Jframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateStatTable() {
        //overall mean,median,mode
        try {
            //Age ----->
            String sql1 = "select avg(Age) as mean_age from Student_data";
            pst = connection.prepareStatement(sql1);
            resultSet = pst.executeQuery();
            float f1 = Float.parseFloat(resultSet.getString("mean_age"));
            String a1 = String.format("%.2f", f1);
            //System.out.println(a);
            txtAMeanOver.setText(a1);

            String sql2 = "SELECT AVG(Age) FROM (SELECT Age FROM Student_data"
                    + " ORDER BY Age LIMIT 2 - (SELECT COUNT(*) FROM Student_data) % 2"
                    + " OFFSET (SELECT (COUNT(*) - 1) / 2"
                    + " FROM Student_data))";
            pst = connection.prepareStatement(sql2);
            resultSet = pst.executeQuery();
            float f2 = Float.parseFloat(resultSet.getString("AVG(Age)"));
            String a2 = String.format("%.2f", f2);
            txtAMediOver.setText(a2);

            String sql3 = "select age,max(c) from(select age,count(*) as c"
                    + " from student_data group by age ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql3);
            resultSet = pst.executeQuery();
            float f3 = Float.parseFloat(resultSet.getString("age"));
            String a3 = String.format("%.2f", f3);
            txtAModeOver.setText(a3);

            //Height ------->
            String sql4 = "select avg(Height) as mean_height from Student_data";
            pst = connection.prepareStatement(sql4);
            resultSet = pst.executeQuery();
            float f4 = Float.parseFloat(resultSet.getString("mean_height"));
            String a4 = String.format("%.2f", f4);
            txtHMeanOver.setText(a4);

            String sql5 = "SELECT AVG(Height) FROM (SELECT Height FROM Student_data"
                    + " ORDER BY Height LIMIT 2 - (SELECT COUNT(*) FROM Student_data) % 2"
                    + " OFFSET (SELECT (COUNT(*) - 1) / 2"
                    + " FROM Student_data))";
            pst = connection.prepareStatement(sql5);
            resultSet = pst.executeQuery();
            float f5 = Float.parseFloat(resultSet.getString("AVG(Height)"));
            String a5 = String.format("%.2f", f5);
            txtHMediOver.setText(a5);

            String sql6 = "select Height,max(c) from(select Height,count(*) as c"
                    + " from student_data group by Height ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql6);
            resultSet = pst.executeQuery();
            float f6 = Float.parseFloat(resultSet.getString("Height"));
            String a6 = String.format("%.2f", f6);
            txtHModeOver.setText(a6);

            //Weight ------->
            String sql7 = "select avg(Weight) as mean_weight from Student_data";
            pst = connection.prepareStatement(sql7);
            resultSet = pst.executeQuery();
            float f7 = Float.parseFloat(resultSet.getString("mean_weight"));
            String a7 = String.format("%.2f", f7);
            txtWMeanOver.setText(a7);

            String sql8 = "SELECT AVG(Weight) FROM (SELECT Weight FROM Student_data"
                    + " ORDER BY Weight LIMIT 2 - (SELECT COUNT(*) FROM Student_data) % 2"
                    + " OFFSET (SELECT (COUNT(*) - 1) / 2"
                    + " FROM Student_data))";
            pst = connection.prepareStatement(sql8);
            resultSet = pst.executeQuery();
            float f8 = Float.parseFloat(resultSet.getString("AVG(Weight)"));
            String a8 = String.format("%.2f", f8);
            txtWMediOver.setText(a8);

            String sql9 = "select Weight,max(c) from(select Weight,count(*) as c"
                    + " from student_data group by Weight ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql9);
            resultSet = pst.executeQuery();
            float f9 = Float.parseFloat(resultSet.getString("Weight"));
            String a9 = String.format("%.2f", f9);
            txtWModeOver.setText(a9);

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

        //Male student mean,median,mode
        try {
            //Age ----->
            String sql1 = "select avg(Age) as mean_age from Student_data where "
                    + "gender='Male'";
            pst = connection.prepareStatement(sql1);
            resultSet = pst.executeQuery();
            float f1 = Float.parseFloat(resultSet.getString("mean_age"));
            String a1 = String.format("%.2f", f1);
            //System.out.println(a);
            txtAMeanMale.setText(a1);

            String sql2 = "SELECT AVG(Age) FROM (SELECT Age FROM Student_data "
                    + "where Gender='Male' "
                    + "ORDER BY Age LIMIT 2 - (SELECT COUNT(*) FROM "
                    + "Student_data where Gender='Male') % 2 "
                    + "OFFSET (SELECT (COUNT(*) - 1) / 2 "
                    + "FROM Student_data where Gender='Male'))";
            pst = connection.prepareStatement(sql2);
            resultSet = pst.executeQuery();
            float f2 = Float.parseFloat(resultSet.getString("AVG(Age)"));
            String a2 = String.format("%.2f", f2);
            txtAMediMale.setText(a2);

            String sql3 = "select age,max(c) from(select age,count(*) as c"
                    + " from student_data where Gender='Male' "
                    + "group by age ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql3);
            resultSet = pst.executeQuery();
            float f3 = Float.parseFloat(resultSet.getString("age"));
            String a3 = String.format("%.2f", f3);
            txtAModeMale.setText(a3);

            //Height ------->
            String sql4 = "select avg(Height) as mean_Height from Student_data where "
                    + "gender='Male'";
            pst = connection.prepareStatement(sql4);
            resultSet = pst.executeQuery();
            float f4 = Float.parseFloat(resultSet.getString("mean_Height"));
            String a4 = String.format("%.2f", f4);
            txtHMeanMale.setText(a4);

            String sql5 = "SELECT AVG(Height) FROM (SELECT Height FROM Student_data "
                    + "where Gender='Male' "
                    + "ORDER BY Height LIMIT 2 - (SELECT COUNT(*) FROM "
                    + "Student_data where Gender='Male') % 2 "
                    + "OFFSET (SELECT (COUNT(*) - 1) / 2 "
                    + "FROM Student_data where Gender='Male'))";
            pst = connection.prepareStatement(sql5);
            resultSet = pst.executeQuery();
            float f5 = Float.parseFloat(resultSet.getString("AVG(Height)"));
            String a5 = String.format("%.2f", f5);
            txtHMediMale.setText(a5);

            String sql6 = "select Height,max(c) from(select Height,count(*) as c"
                    + " from student_data where Gender='Male' "
                    + "group by Height ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql6);
            resultSet = pst.executeQuery();
            float f6 = Float.parseFloat(resultSet.getString("Height"));
            String a6 = String.format("%.2f", f6);
            txtHModeMale.setText(a6);

            //Weight ------->
            String sql7 = "select avg(Weight) as mean_Weight from Student_data where "
                    + "gender='Male'";
            pst = connection.prepareStatement(sql7);
            resultSet = pst.executeQuery();
            float f7 = Float.parseFloat(resultSet.getString("mean_Weight"));
            String a7 = String.format("%.2f", f7);
            txtWMeanMale.setText(a7);

            String sql8 = "SELECT AVG(Weight) FROM (SELECT Weight FROM Student_data "
                    + "where Gender='Male' "
                    + "ORDER BY Weight LIMIT 2 - (SELECT COUNT(*) FROM "
                    + "Student_data where Gender='Male') % 2 "
                    + "OFFSET (SELECT (COUNT(*) - 1) / 2 "
                    + "FROM Student_data where Gender='Male'))";
            pst = connection.prepareStatement(sql8);
            resultSet = pst.executeQuery();
            float f8 = Float.parseFloat(resultSet.getString("AVG(Weight)"));
            String a8 = String.format("%.2f", f8);
            txtWMediMale.setText(a8);

            String sql9 = "select Weight,max(c) from(select Weight,count(*) as c"
                    + " from student_data where Gender='Male' "
                    + "group by Weight ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql9);
            resultSet = pst.executeQuery();
            float f9 = Float.parseFloat(resultSet.getString("Weight"));
            String a9 = String.format("%.2f", f9);
            txtWModeMale.setText(a9);

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

        //Female student mean,median,mode
        try {
            //Age ----->
            String sql1 = "select avg(Age) as mean_age from Student_data where "
                    + "gender='Female'";
            pst = connection.prepareStatement(sql1);
            resultSet = pst.executeQuery();
            float f1 = Float.parseFloat(resultSet.getString("mean_age"));
            String a1 = String.format("%.2f", f1);
            //System.out.println(a);
            txtAMeanFemale.setText(a1);

            String sql2 = "SELECT AVG(Age) FROM (SELECT Age FROM Student_data "
                    + "where Gender='Female' "
                    + "ORDER BY Age LIMIT 2 - (SELECT COUNT(*) FROM "
                    + "Student_data where Gender='Female') % 2 "
                    + "OFFSET (SELECT (COUNT(*) - 1) / 2 "
                    + "FROM Student_data where Gender='Female'))";
            pst = connection.prepareStatement(sql2);
            resultSet = pst.executeQuery();
            float f2 = Float.parseFloat(resultSet.getString("AVG(Age)"));
            String a2 = String.format("%.2f", f2);
            txtAMediFemale.setText(a2);

            String sql3 = "select age,max(c) from(select age,count(*) as c"
                    + " from student_data where Gender='Female' "
                    + "group by age ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql3);
            resultSet = pst.executeQuery();
            float f3 = Float.parseFloat(resultSet.getString("age"));
            String a3 = String.format("%.2f", f3);
            txtAModeFemale.setText(a3);

            //Height ------->
            String sql4 = "select avg(Height) as mean_Height from Student_data where "
                    + "gender='Female'";
            pst = connection.prepareStatement(sql4);
            resultSet = pst.executeQuery();
            float f4 = Float.parseFloat(resultSet.getString("mean_Height"));
            String a4 = String.format("%.2f", f4);
            txtHMeanFemale.setText(a4);

            String sql5 = "SELECT AVG(Height) FROM (SELECT Height FROM Student_data "
                    + "where Gender='Female' "
                    + "ORDER BY Height LIMIT 2 - (SELECT COUNT(*) FROM "
                    + "Student_data where Gender='Female') % 2 "
                    + "OFFSET (SELECT (COUNT(*) - 1) / 2 "
                    + "FROM Student_data where Gender='Female'))";
            pst = connection.prepareStatement(sql5);
            resultSet = pst.executeQuery();
            float f5 = Float.parseFloat(resultSet.getString("AVG(Height)"));
            String a5 = String.format("%.2f", f5);
            txtHMediFemale.setText(a5);

            String sql6 = "select Height,max(c) from(select Height,count(*) as c"
                    + " from student_data where Gender='Female' "
                    + "group by Height ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql6);
            resultSet = pst.executeQuery();
            float f6 = Float.parseFloat(resultSet.getString("Height"));
            String a6 = String.format("%.2f", f6);
            txtHModeFemale.setText(a6);

            //Weight ------->
            String sql7 = "select avg(Weight) as mean_Weight from Student_data where "
                    + "gender='Female'";
            pst = connection.prepareStatement(sql7);
            resultSet = pst.executeQuery();
            float f7 = Float.parseFloat(resultSet.getString("mean_Weight"));
            String a7 = String.format("%.2f", f7);
            txtWMeanFemale.setText(a7);

            String sql8 = "SELECT AVG(Weight) FROM (SELECT Weight FROM Student_data "
                    + "where Gender='Female' "
                    + "ORDER BY Weight LIMIT 2 - (SELECT COUNT(*) FROM "
                    + "Student_data where Gender='Female') % 2 "
                    + "OFFSET (SELECT (COUNT(*) - 1) / 2 "
                    + "FROM Student_data where Gender='Female'))";
            pst = connection.prepareStatement(sql8);
            resultSet = pst.executeQuery();
            float f8 = Float.parseFloat(resultSet.getString("AVG(Weight)"));
            String a8 = String.format("%.2f", f8);
            txtWMediFemale.setText(a8);

            String sql9 = "select Weight,max(c) from(select Weight,count(*) as c"
                    + " from student_data where Gender='Female' "
                    + "group by Weight ORDER BY COUNT(*) DESC)";
            pst = connection.prepareStatement(sql9);
            resultSet = pst.executeQuery();
            float f9 = Float.parseFloat(resultSet.getString("Weight"));
            String a9 = String.format("%.2f", f9);
            txtWModeFemale.setText(a9);

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

        //Total students Calculations
        try {
            String sql1 = "SELECT count(*)  FROM Student_data where Gender='Male'";
            pst = connection.prepareStatement(sql1);
            resultSet = pst.executeQuery();
            labelTotalMale.setText(resultSet.getString("count(*)"));

            String sql2 = "SELECT count(*)  FROM Student_data where Gender='Female'";
            pst = connection.prepareStatement(sql2);
            resultSet = pst.executeQuery();
            labelTotalFemale.setText(resultSet.getString("count(*)"));

            String sql3 = "SELECT count(*)  FROM Student_data";
            pst = connection.prepareStatement(sql3);
            resultSet = pst.executeQuery();
            labelTotalStudent.setText(resultSet.getString("count(*)"));
        } catch (Exception e) {
        }

    }

    private void getEachStDeviation() {
        labelDeviation.setText(txtStId.getText());
        float a;
        float b;
        String s;
        //Age--->
        a = Float.parseFloat(txtAMeanOver.getText());
        b = Float.parseFloat(txtStAge.getText());
        a = a - b;
        //System.out.println(a);
        s = String.format("%.2f", a);
        txtAMeanEach.setText(s);
        
        a = Float.parseFloat(txtAMediOver.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtAMediEach.setText(s);
        
        a = Float.parseFloat(txtAModeOver.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtAModeEach.setText(s);
        
        
        ///Height--->
        a = Float.parseFloat(txtHMeanOver.getText());
        b = Float.parseFloat(txtStHeight.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtHMeanEach.setText(s);
        
        a = Float.parseFloat(txtHMediOver.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtHMediEach.setText(s);
        
        a = Float.parseFloat(txtHModeOver.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtHModeEach.setText(s);
        
        //Weight---->
        a = Float.parseFloat(txtWMeanOver.getText());
        b = Float.parseFloat(txtStWeight.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtWMeanEach.setText(s);
        
        a = Float.parseFloat(txtWMediOver.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtWMediEach.setText(s);
        
        a = Float.parseFloat(txtWModeOver.getText());
        a = a - b;
        s = String.format("%.2f", a);
        txtWModeEach.setText(s);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        buttonAbout1 = new javax.swing.JButton();
        buttonAbout = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        buttonHelp = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        buttonAdd = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonEdit = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        panelStInfo = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtStWeight = new javax.swing.JTextField();
        txtStHeight = new javax.swing.JTextField();
        txtStAge = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtStBlood = new javax.swing.JTextField();
        txtStContact = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtStId = new javax.swing.JTextField();
        txtStFName = new javax.swing.JTextField();
        txtStLName = new javax.swing.JTextField();
        txtStDept = new javax.swing.JTextField();
        comboGender = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        txtImgPath = new javax.swing.JTextField();
        buttonUpload = new javax.swing.JButton();
        buttonImgSave = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        labelImage = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableStudentData = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        sliderAge = new javax.swing.JSlider();
        jLabel18 = new javax.swing.JLabel();
        sliderWeight = new javax.swing.JSlider();
        jLabel19 = new javax.swing.JLabel();
        sliderHeight = new javax.swing.JSlider();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tAreaComment = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        buttonReport = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        buttonEachBar = new javax.swing.JButton();
        buttonEachPie = new javax.swing.JButton();
        buttonEachLine = new javax.swing.JButton();
        buttonEachOver = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        buttonOvrallBar = new javax.swing.JButton();
        buttonOvrallLine = new javax.swing.JButton();
        comboChart = new javax.swing.JComboBox();
        buttonOvrallOver = new javax.swing.JButton();
        buttonCapture = new javax.swing.JButton();
        panelChart = new javax.swing.JPanel();
        labelChart = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        txtAMeanOver = new javax.swing.JTextField();
        txtAMediOver = new javax.swing.JTextField();
        txtAModeOver = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        txtHMeanOver = new javax.swing.JTextField();
        txtHMediOver = new javax.swing.JTextField();
        txtHModeOver = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        txtWMeanOver = new javax.swing.JTextField();
        txtWMediOver = new javax.swing.JTextField();
        txtWModeOver = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        labelDeviation = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        txtAMeanEach = new javax.swing.JTextField();
        txtAMediEach = new javax.swing.JTextField();
        txtAModeEach = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        txtHMeanEach = new javax.swing.JTextField();
        txtHMediEach = new javax.swing.JTextField();
        txtHModeEach = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        txtWMeanEach = new javax.swing.JTextField();
        txtWMediEach = new javax.swing.JTextField();
        txtWModeEach = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        txtAMeanMale = new javax.swing.JTextField();
        txtAMediMale = new javax.swing.JTextField();
        txtAModeMale = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        txtHMeanMale = new javax.swing.JTextField();
        txtHMediMale = new javax.swing.JTextField();
        txtHModeMale = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        txtWMeanMale = new javax.swing.JTextField();
        txtWMediMale = new javax.swing.JTextField();
        txtWModeMale = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        labelTotalStudent = new javax.swing.JLabel();
        labelTotalFemale = new javax.swing.JLabel();
        labelTotalMale = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        txtAMeanFemale = new javax.swing.JTextField();
        txtAMediFemale = new javax.swing.JTextField();
        txtAModeFemale = new javax.swing.JTextField();
        jPanel41 = new javax.swing.JPanel();
        txtHMeanFemale = new javax.swing.JTextField();
        txtHMediFemale = new javax.swing.JTextField();
        txtHModeFemale = new javax.swing.JTextField();
        jPanel42 = new javax.swing.JPanel();
        txtWMeanFemale = new javax.swing.JTextField();
        txtWMediFemale = new javax.swing.JTextField();
        txtWModeFemale = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableDocument = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        buttonDocDel = new javax.swing.JButton();
        txtDocDocName = new javax.swing.JTextField();
        buttonDocAttach = new javax.swing.JButton();
        txtDocStudentId = new javax.swing.JTextField();
        txtDocAttach1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        buttonDocAdd = new javax.swing.JButton();
        txtDocDocId = new javax.swing.JTextField();
        buttonDocClear = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        txtFrom = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        buttonMail = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtTo = new javax.swing.JTextField();
        txtAttachName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtpassword = new javax.swing.JPasswordField();
        buttonAttach = new javax.swing.JButton();
        txtDocAttach = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableIdentify = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuNew = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        menuHelp = new javax.swing.JMenuItem();
        webHelp = new javax.swing.JMenuItem();
        Jmenu5 = new javax.swing.JMenu();
        menuAbout = new javax.swing.JMenuItem();
        menuDate = new javax.swing.JMenu();
        menuTime = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 51));

        jToolBar1.setRollover(true);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton1.setText(" Sign Out ");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        buttonAbout1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buttonAbout1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Help.png"))); // NOI18N
        buttonAbout1.setText(" Help ");
        buttonAbout1.setFocusable(false);
        buttonAbout1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAbout1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAbout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAbout1ActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonAbout1);

        buttonAbout.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buttonAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about.png"))); // NOI18N
        buttonAbout.setText(" About ");
        buttonAbout.setFocusable(false);
        buttonAbout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAbout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAboutActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonAbout);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Welcome  To  Easy Stats   System ");

        txtSearch.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtSearch.setText("   Search.....");
        txtSearch.setToolTipText("Search by Employee Id or Name");
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        buttonHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Search.png"))); // NOI18N
        buttonHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHelpActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Commands", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Agency FB", 1, 14), new java.awt.Color(0, 51, 102))); // NOI18N

        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png"))); // NOI18N
        buttonAdd.setText("Add");
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        buttonDelete.setText("Delete");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        buttonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-icon.png"))); // NOI18N
        buttonEdit.setText("Edit");
        buttonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditActionPerformed(evt);
            }
        });

        buttonClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Clear.png"))); // NOI18N
        buttonClear.setText("Clear");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(buttonAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClear)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonHelp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelStInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Student Info", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Agency FB", 1, 14), new java.awt.Color(0, 0, 102))); // NOI18N
        panelStInfo.setForeground(new java.awt.Color(0, 51, 102));

        jLabel7.setText("Age");

        jLabel8.setText("Height");

        jLabel9.setText("Weight");

        jLabel10.setText("Gender");

        jLabel11.setText("Blood");

        jLabel12.setText("Series");

        jLabel13.setText("Department");

        jLabel14.setText("Last Name");

        jLabel15.setText("First Name");

        jLabel16.setText("Student Id");

        comboGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        javax.swing.GroupLayout panelStInfoLayout = new javax.swing.GroupLayout(panelStInfo);
        panelStInfo.setLayout(panelStInfoLayout);
        panelStInfoLayout.setHorizontalGroup(
            panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStInfoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtStContact)
                        .addComponent(txtStDept, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtStLName)
                        .addComponent(txtStFName)
                        .addComponent(txtStId, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtStBlood, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtStWeight, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtStHeight, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtStAge, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(comboGender, 0, 154, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        panelStInfoLayout.setVerticalGroup(
            panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelStInfoLayout.createSequentialGroup()
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(14, 14, 14)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtStWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(comboGender, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStBlood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(panelStInfoLayout.createSequentialGroup()
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(14, 14, 14)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtStLName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtStDept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(panelStInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Attach.png"))); // NOI18N
        buttonUpload.setText("Upload");
        buttonUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUploadActionPerformed(evt);
            }
        });

        buttonImgSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        buttonImgSave.setText("Save");
        buttonImgSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonImgSaveActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(labelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(labelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonImgSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(txtImgPath, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonUpload)
                    .addComponent(txtImgPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonImgSave)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(102, 102, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Action Panel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Agency FB", 1, 14), new java.awt.Color(0, 0, 102))); // NOI18N

        jTabbedPane7.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        tableStudentData.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tableStudentData.setForeground(new java.awt.Color(0, 0, 51));
        tableStudentData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableStudentData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableStudentDataMouseClicked(evt);
            }
        });
        tableStudentData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableStudentDataKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tableStudentData);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Indicator", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 0, 14), new java.awt.Color(0, 0, 102))); // NOI18N
        jPanel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Age");

        sliderAge.setBackground(new java.awt.Color(153, 153, 255));
        sliderAge.setMajorTickSpacing(20);
        sliderAge.setMinorTickSpacing(5);
        sliderAge.setPaintLabels(true);
        sliderAge.setPaintTicks(true);
        sliderAge.setValue(0);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("Height");

        sliderWeight.setBackground(new java.awt.Color(153, 153, 255));
        sliderWeight.setMajorTickSpacing(20);
        sliderWeight.setMinorTickSpacing(5);
        sliderWeight.setPaintLabels(true);
        sliderWeight.setPaintTicks(true);
        sliderWeight.setValue(0);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setText("Weight");

        sliderHeight.setBackground(new java.awt.Color(153, 153, 255));
        sliderHeight.setMajorTickSpacing(2);
        sliderHeight.setMaximum(10);
        sliderHeight.setMinorTickSpacing(1);
        sliderHeight.setPaintLabels(true);
        sliderHeight.setPaintTicks(true);
        sliderHeight.setValue(0);
        sliderHeight.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sliderAge, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sliderWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(sliderHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel3)))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel18))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sliderHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel19)))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(204, 204, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Report", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 0, 14), new java.awt.Color(0, 0, 102))); // NOI18N

        tAreaComment.setColumns(20);
        tAreaComment.setRows(5);
        jScrollPane4.setViewportView(tAreaComment);

        jLabel21.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 102));
        jLabel21.setText("Comment");

        jLabel22.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 102));
        jLabel22.setText("Health Status");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jRadioButton1.setForeground(new java.awt.Color(0, 153, 51));
        jRadioButton1.setText("Good");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jRadioButton2.setForeground(new java.awt.Color(204, 0, 0));
        jRadioButton2.setText("Not Good");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonReport.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buttonReport.setForeground(new java.awt.Color(0, 0, 102));
        buttonReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/report.png"))); // NOI18N
        buttonReport.setText("Generate Report");
        buttonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReportActionPerformed(evt);
            }
        });

        buttonGroup2.add(jCheckBox1);
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(0, 0, 102));
        jCheckBox1.setText("Include Chart");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jCheckBox2);
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(0, 0, 102));
        jCheckBox2.setText("No Chart");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 102));
        jLabel23.setText("Attachment");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                                .addComponent(jLabel21))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttonReport, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonReport)))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane7.addTab(" Data Table ", jPanel8);

        jPanel9.setBackground(new java.awt.Color(204, 204, 255));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Each Student", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 14), new java.awt.Color(0, 51, 102))); // NOI18N

        buttonEachBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bar1.png"))); // NOI18N
        buttonEachBar.setText("Bar Chart");
        buttonEachBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEachBarActionPerformed(evt);
            }
        });

        buttonEachPie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pie1.png"))); // NOI18N
        buttonEachPie.setText("Pie Chart");
        buttonEachPie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEachPieActionPerformed(evt);
            }
        });

        buttonEachLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line1.png"))); // NOI18N
        buttonEachLine.setText("Line Chart");
        buttonEachLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEachLineActionPerformed(evt);
            }
        });

        buttonEachOver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bar1.png"))); // NOI18N
        buttonEachOver.setText("Overlaid");
        buttonEachOver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEachOverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonEachBar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonEachLine, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonEachPie, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonEachOver, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonEachPie, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonEachBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonEachLine, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonEachOver, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Overall", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 14), new java.awt.Color(0, 51, 102))); // NOI18N

        buttonOvrallBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bar1.png"))); // NOI18N
        buttonOvrallBar.setText("Bar Chart");
        buttonOvrallBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOvrallBarActionPerformed(evt);
            }
        });

        buttonOvrallLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line1.png"))); // NOI18N
        buttonOvrallLine.setText("Line Chart");
        buttonOvrallLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOvrallLineActionPerformed(evt);
            }
        });

        comboChart.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Age", "Height", "Weight" }));

        buttonOvrallOver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bar1.png"))); // NOI18N
        buttonOvrallOver.setText("Overlaid");
        buttonOvrallOver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOvrallOverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(comboChart, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonOvrallBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonOvrallLine, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addComponent(buttonOvrallOver, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(comboChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonOvrallBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonOvrallLine, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonOvrallOver, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        buttonCapture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/capture1.png"))); // NOI18N
        buttonCapture.setText("Capture");
        buttonCapture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCaptureActionPerformed(evt);
            }
        });

        labelChart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/chart.png"))); // NOI18N
        labelChart.setText("Chart");

        javax.swing.GroupLayout panelChartLayout = new javax.swing.GroupLayout(panelChart);
        panelChart.setLayout(panelChartLayout);
        panelChartLayout.setHorizontalGroup(
            panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChartLayout.createSequentialGroup()
                .addContainerGap(177, Short.MAX_VALUE)
                .addComponent(labelChart, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(160, 160, 160))
        );
        panelChartLayout.setVerticalGroup(
            panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChartLayout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(labelChart, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(126, 126, 126)
                .addComponent(buttonCapture, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(222, 222, 222)
                .addComponent(panelChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(buttonCapture, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab(" Chart ", jPanel9);

        jPanel10.setBackground(new java.awt.Color(204, 204, 255));

        jPanel18.setBackground(new java.awt.Color(204, 204, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel19.setBackground(new java.awt.Color(102, 102, 255));

        jLabel24.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("OverAll");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addContainerGap())
        );

        jLabel25.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 102));
        jLabel25.setText("   Mean ");

        jLabel26.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 102));
        jLabel26.setText(" Median ");

        jLabel27.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 102));
        jLabel27.setText("   Mode");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAMeanOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAMediOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(txtAModeOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtAMeanOver)
                .addComponent(txtAMediOver)
                .addComponent(txtAModeOver))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtHMeanOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHMediOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtHModeOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtHMeanOver)
                .addComponent(txtHMediOver)
                .addComponent(txtHModeOver))
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtWMeanOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtWMediOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtWModeOver, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtWMeanOver)
                .addComponent(txtWMediOver)
                .addComponent(txtWModeOver))
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel28.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 102));
        jLabel28.setText("Age");

        jLabel29.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 102));
        jLabel29.setText("    Height");

        jLabel30.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 102));
        jLabel30.setText("   Weight");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel28)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel28)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel29)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel30)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel24.setBackground(new java.awt.Color(204, 204, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel25.setBackground(new java.awt.Color(102, 102, 255));

        labelDeviation.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        labelDeviation.setForeground(new java.awt.Color(255, 255, 255));
        labelDeviation.setText("     Each");

        jLabel54.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Deviation");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelDeviation, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addGap(42, 42, 42))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDeviation)
                    .addComponent(jLabel54))
                .addContainerGap())
        );

        jLabel34.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 102));
        jLabel34.setText("   Mean ");

        jLabel35.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 102));
        jLabel35.setText(" Median ");

        jLabel36.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 102));
        jLabel36.setText("   Mode");

        txtAMeanEach.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtAMediEach.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtAModeEach.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAMeanEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAMediEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(txtAModeEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtAMeanEach)
                .addComponent(txtAMediEach)
                .addComponent(txtAModeEach))
        );

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtHMeanEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHMediEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtHModeEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtHMeanEach)
                .addComponent(txtHMediEach)
                .addComponent(txtHModeEach))
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtWMeanEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtWMediEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtWModeEach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtWMeanEach)
                .addComponent(txtWMediEach)
                .addComponent(txtWModeEach))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel37.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 102));
        jLabel37.setText("Age");

        jLabel38.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 102));
        jLabel38.setText("    Height");

        jLabel39.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 102));
        jLabel39.setText("   Weight");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel37)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel37)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel38)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel39)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel30.setBackground(new java.awt.Color(204, 204, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel31.setBackground(new java.awt.Color(102, 102, 255));

        jLabel40.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Male Student");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addContainerGap())
        );

        jLabel41.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 102));
        jLabel41.setText("   Mean ");

        jLabel42.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 102));
        jLabel42.setText(" Median ");

        jLabel43.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 102));
        jLabel43.setText("   Mode");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAMeanMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAMediMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(txtAModeMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtAMeanMale)
                .addComponent(txtAMediMale)
                .addComponent(txtAModeMale))
        );

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtHMeanMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHMediMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtHModeMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtHMeanMale)
                .addComponent(txtHMediMale)
                .addComponent(txtHModeMale))
        );

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtWMeanMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtWMediMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtWModeMale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtWMeanMale)
                .addComponent(txtWMediMale)
                .addComponent(txtWModeMale))
        );

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel44.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 0, 102));
        jLabel44.setText("Age");

        jLabel45.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 102));
        jLabel45.setText("    Height");

        jLabel46.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 0, 102));
        jLabel46.setText("   Weight");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel44)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel44)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel45)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel46)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jLabel55.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel55.setText("Male Student");

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel56.setText("=");

        jLabel57.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel57.setText("Female Student");

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel58.setText("=");

        jLabel59.setText("____________________________________");

        jLabel60.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel60.setText("Total");

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel61.setText("=");

        labelTotalStudent.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N

        labelTotalFemale.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N

        labelTotalMale.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel59)
                            .addGroup(jPanel36Layout.createSequentialGroup()
                                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel36Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel55)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel56))
                                    .addGroup(jPanel36Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel57)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelTotalFemale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelTotalMale, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)))))
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTotalStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTotalMale, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel55)
                        .addComponent(jLabel56)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTotalFemale, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel58)
                        .addComponent(jLabel57)))
                .addGap(13, 13, 13)
                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel60)
                        .addComponent(jLabel61))
                    .addComponent(labelTotalStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(86, 86, 86))
        );

        jPanel37.setBackground(new java.awt.Color(204, 204, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel38.setBackground(new java.awt.Color(102, 102, 255));

        jLabel47.setFont(new java.awt.Font("Agency FB", 1, 16)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Female Student");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel47)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addContainerGap())
        );

        jLabel48.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 102));
        jLabel48.setText("   Mean ");

        jLabel49.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 102));
        jLabel49.setText(" Median ");

        jLabel50.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 102));
        jLabel50.setText("   Mode");

        txtAMeanFemale.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtAMediFemale.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtAModeFemale.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAMeanFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAMediFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(txtAModeFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtAMeanFemale)
                .addComponent(txtAMediFemale)
                .addComponent(txtAModeFemale))
        );

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtHMeanFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHMediFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtHModeFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtHMeanFemale)
                .addComponent(txtHMediFemale)
                .addComponent(txtHModeFemale))
        );

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtWMeanFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtWMediFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtWModeFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtWMeanFemale)
                .addComponent(txtWMediFemale)
                .addComponent(txtWModeFemale))
        );

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel51.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 102));
        jLabel51.setText("Age");

        jLabel52.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 102));
        jLabel52.setText("    Height");

        jLabel53.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 102));
        jLabel53.setText("   Weight");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel51)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel51)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel52)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel53)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane7.addTab(" Statistic", jPanel10);

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("Doc Name");

        tableDocument.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tableDocument.setForeground(new java.awt.Color(0, 0, 51));
        tableDocument.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableDocument.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDocumentMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableDocument);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Doc Id");

        buttonDocDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        buttonDocDel.setText("Delete");
        buttonDocDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDocDelActionPerformed(evt);
            }
        });

        buttonDocAttach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Attach.png"))); // NOI18N
        buttonDocAttach.setText("Attach");
        buttonDocAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDocAttachActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setText("Studnet Id");

        buttonDocAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png"))); // NOI18N
        buttonDocAdd.setText("Add");
        buttonDocAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDocAddActionPerformed(evt);
            }
        });

        buttonDocClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Clear.png"))); // NOI18N
        buttonDocClear.setText("Clear");
        buttonDocClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDocClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDocDocId)
                            .addComponent(txtDocDocName)
                            .addComponent(txtDocStudentId, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonDocDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonDocAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonDocClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtDocAttach1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonDocAttach, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDocAttach1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonDocAttach))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonDocAdd)
                            .addComponent(txtDocDocId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonDocDel)
                            .addComponent(jLabel31)
                            .addComponent(txtDocStudentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDocDocName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32)
                            .addComponent(buttonDocClear))))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab(" Document ", jPanel11);

        jPanel12.setBackground(new java.awt.Color(204, 204, 255));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 0));
        jLabel6.setText("TO");

        jLabel20.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 153));
        jLabel20.setText("      Attachment Name");

        buttonMail.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buttonMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mail.png"))); // NOI18N
        buttonMail.setText("Send Mail");
        buttonMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMailActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("Password");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 51, 0));
        jLabel17.setText("SUBJECT");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("FROM");

        txtpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpasswordActionPerformed(evt);
            }
        });

        buttonAttach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Attach.png"))); // NOI18N
        buttonAttach.setText("Attach");
        buttonAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAttachActionPerformed(evt);
            }
        });

        txtDocAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocAttachActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel6)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(txtTo)
                    .addComponent(txtFrom)
                    .addComponent(txtpassword, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(31, 31, 31)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDocAttach)
                            .addComponent(txtAttachName, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonAttach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(buttonMail, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17))
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDocAttach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonAttach)))
                .addGap(13, 13, 13)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAttachName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonMail, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(204, 204, 204)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(226, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab(" Email ", jPanel12);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane7)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane7)
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tableIdentify.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        tableIdentify.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableIdentify.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableIdentifyMouseClicked(evt);
            }
        });
        tableIdentify.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableIdentifyKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableIdentify);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("  File");

        menuNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/newfile.png"))); // NOI18N
        menuNew.setText("New");
        menuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewActionPerformed(evt);
            }
        });
        jMenu1.add(menuNew);
        jMenu1.add(jSeparator1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jMenuItem2.setText("Close");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        menuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        menuExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        jMenu1.add(menuExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(" Edit");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/snip.png"))); // NOI18N
        jMenuItem1.setText("Snip");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText(" Help");

        menuHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        menuHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Help.png"))); // NOI18N
        menuHelp.setText("Help File");
        menuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHelpActionPerformed(evt);
            }
        });
        jMenu3.add(menuHelp);

        webHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        webHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/webHelp.png"))); // NOI18N
        webHelp.setText("Web Help");
        webHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webHelpActionPerformed(evt);
            }
        });
        jMenu3.add(webHelp);

        jMenuBar1.add(jMenu3);

        Jmenu5.setText(" About");

        menuAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        menuAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/about.png"))); // NOI18N
        menuAbout.setText("About");
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAboutActionPerformed(evt);
            }
        });
        Jmenu5.add(menuAbout);

        jMenuBar1.add(Jmenu5);

        menuDate.setText(" Date ");
        jMenuBar1.add(menuDate);

        menuTime.setText(" Time ");
        jMenuBar1.add(menuTime);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelStInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelStInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewActionPerformed
        // TODO add your handling code here:
        Stat_Jframe d = new Stat_Jframe();
        d.setVisible(true);
    }//GEN-LAST:event_menuNewActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        //menu item close

        try {
            close();
            LogIn_Jframe a = new LogIn_Jframe();
            a.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        } finally {
            try {
                resultSet.close();
                pst.close();
                connection.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_menuExitActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        try {
            saveScreenShot(panelStInfo, "Panel Img.png");
            JOptionPane.showMessageDialog(rootPane, "Image is Captured");
        } catch (Exception ex) {
            Logger.getLogger(Stat_Jframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHelpActionPerformed
        // TODO add your handling code here:
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "Files\\DSP.pdf");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error");
        }
    }//GEN-LAST:event_menuHelpActionPerformed

    private void webHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webHelpActionPerformed
        // TODO add your handling code here:
        try {
            String url = "http://www.youtube.com/";
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_webHelpActionPerformed

    private void menuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAboutActionPerformed
        // TODO add your handling code here:
        try {
            About_Jframe ab = new About_Jframe();
            ab.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_menuAboutActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            close();
            LogIn_Jframe a = new LogIn_Jframe();
            a.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        } finally {
            try {
                resultSet.close();
                pst.close();
                connection.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonAbout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAbout1ActionPerformed
        // TODO add your handling code here:
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "Files\\DSP.pdf");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error");
        }
    }//GEN-LAST:event_buttonAbout1ActionPerformed

    private void buttonAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAboutActionPerformed
        // TODO add your handling code here:
        try {
            About_Jframe ab = new About_Jframe();
            ab.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonAboutActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        try {
            String sql = "select * from Student_data where First_name=?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, txtSearch.getText());
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                getValue();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
        try {
            String sql = "select * from Student_data where Student_id=?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, txtSearch.getText());
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                getValue();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void buttonHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHelpActionPerformed
        // TODO add your handling code here:
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "Files\\DSP.pdf");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error");
        }
    }//GEN-LAST:event_buttonHelpActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        // TODO add your handling code here:
        String txtStGender = (String) comboGender.getSelectedItem();
        try {
            String sql = "insert into Student_data(Student_id,First_name,Last_name,"
                    + "Department,Series,Age,Height,Weight,Gender,Blood)"
                    + "values(?,?,?,?,?,?,?,?,?,?) ";
            pst = connection.prepareStatement(sql);
            pst.setString(1, txtStId.getText());
            pst.setString(2, txtStFName.getText());
            pst.setString(3, txtStLName.getText());
            pst.setString(4, txtStDept.getText());
            pst.setString(5, txtStContact.getText());
            pst.setString(6, txtStAge.getText());
            pst.setString(7, txtStHeight.getText());
            pst.setString(8, txtStWeight.getText());
            pst.setString(9, txtStGender);
            pst.setString(10, txtStBlood.getText());

            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

        updateStudentDataTable();
        updateTableIdentify();
        updateStatTable();
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        // TODO add your handling code here:
        int p = JOptionPane.showConfirmDialog(rootPane, "Do you really want to delete?", "Delete", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            String sql = "delete from Student_data where Student_id = ?";
            try {
                pst = connection.prepareStatement(sql);
                pst.setString(1, txtStId.getText());
                pst.execute();

                JOptionPane.showMessageDialog(rootPane, "Deleted");
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }
        updateStudentDataTable();
        updateTableIdentify();
        updateStatTable();
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void buttonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditActionPerformed
        // TODO add your handling code here:
        String txtStGender = (String) comboGender.getSelectedItem();
        try {
            String value1 = txtStId.getText();
            String value2 = txtStFName.getText();
            String value3 = txtStLName.getText();
            String value4 = txtStDept.getText();
            String value5 = txtStContact.getText();
            String value6 = txtStAge.getText();
            String value7 = txtStHeight.getText();
            String value8 = txtStWeight.getText();
            String value9 = txtStGender;
            String value10 = txtStBlood.getText();
            String sql = "update Student_data set Student_id='" + value1 + "',First_name='" + value2 + "'"
                    + ",Last_name='" + value3 + "',Department='" + value4 + "',Series='" + value5 + "',"
                    + "Age='" + value6 + "',Height='" + value7 + "',Weight='" + value8 + "',"
                    + "Gender='" + value9 + "',Blood='" + value10 + "' where Student_id='" + value1 + "'  ";
            pst = connection.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Updated");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
        updateStudentDataTable();
        updateTableIdentify();
        updateStatTable();
        getEachStDeviation();
        
    }//GEN-LAST:event_buttonEditActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        // TODO add your handling code here:
        txtStId.setText("");
        txtStFName.setText("");
        txtStLName.setText("");
        txtStDept.setText("");
        txtStContact.setText("");
        txtStAge.setText("");
        txtStHeight.setText("");
        txtStWeight.setText("");
        comboGender.setSelectedItem("Male");
        txtStBlood.setText("");
        labelImage.setIcon(null);
        
        labelDeviation.setText("     Each");
        txtAMeanEach.setText(null);
        txtAMediEach.setText(null);
        txtAModeEach.setText(null);
        txtHMeanEach.setText(null);
        txtHMediEach.setText(null);
        txtHModeEach.setText(null);
        txtWMeanEach.setText(null);
        txtWMediEach.setText(null);
        txtWModeEach.setText(null);
    }//GEN-LAST:event_buttonClearActionPerformed

    private void buttonImgSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonImgSaveActionPerformed
        // TODO add your handling code here:
        try {
            String value1 = txtStId.getText();
            String value2 = txtImgPath.getText();
            String sql = "update Student_data set Image=? where Student_id='" + value1 + "'  ";
            pst = connection.prepareStatement(sql);
            pst.setBytes(1, personImg);
            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Image Saved.");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

    }//GEN-LAST:event_buttonImgSaveActionPerformed

    private void buttonUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUploadActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        fileName = f.getAbsolutePath();
        txtImgPath.setText(fileName);
        try {
            File fImg = new File(fileName);
            FileInputStream fIS = new FileInputStream(fImg);
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fIS.read(buf)) != -1;) {
                bAOS.write(buf, 0, readNum);
            }
            personImg = bAOS.toByteArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonUploadActionPerformed

    private void tableStudentDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableStudentDataKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            try {
                int row = tableStudentData.getSelectedRow();
                String tableClick = (tableStudentData.getModel().getValueAt(row, 0).toString());
                String sql = "select * from Student_data where Student_id= '" + tableClick + "' ";
                pst = connection.prepareStatement(sql);
                resultSet = pst.executeQuery();
                if (resultSet.next()) {
                    getValue();
                    showSliderData();
                    getChartData();
                    getEachStDeviation();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }
    }//GEN-LAST:event_tableStudentDataKeyReleased

    private void tableStudentDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableStudentDataMouseClicked
        // TODO add your handling code here:
        try {
            int row = tableStudentData.getSelectedRow();
            String tableClick = (tableStudentData.getModel().getValueAt(row, 0).toString());
            String sql = "select * from Student_data where Student_id= '" + tableClick + "' ";
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                getValue();
                showSliderData();
                getChartData();
                getEachStDeviation();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_tableStudentDataMouseClicked

    private void tableIdentifyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableIdentifyKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            try {
                int row = tableIdentify.getSelectedRow();
                String tableClick = (tableIdentify.getModel().getValueAt(row, 0).toString());
                String sql = "select * from Student_data where Student_id= '" + tableClick + "' ";
                pst = connection.prepareStatement(sql);
                resultSet = pst.executeQuery();
                if (resultSet.next()) {
                    getValue();
                    showSliderData();
                    getChartData();
                    getEachStDeviation();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }
    }//GEN-LAST:event_tableIdentifyKeyReleased

    private void tableIdentifyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableIdentifyMouseClicked
        // TODO add your handling code here:
        try {
            int row = tableIdentify.getSelectedRow();
            String tableClick = (tableIdentify.getModel().getValueAt(row, 0).toString());
            String sql = "select * from Student_data where Student_id= '" + tableClick + "' ";
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                getValue();
                showSliderData();
                getChartData();
                getEachStDeviation();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_tableIdentifyMouseClicked

    private void txtpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpasswordActionPerformed

    private void buttonAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAttachActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(rootPane);
        File f = chooser.getSelectedFile();
        //File f= chooser.getCurrentDirectory();
        filePath = f.getAbsolutePath();
        txtDocAttach.setText(filePath);
        txtAttachName.setText(filePath);
    }//GEN-LAST:event_buttonAttachActionPerformed

    private void txtDocAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocAttachActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocAttachActionPerformed

    private void buttonMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMailActionPerformed
        // TODO add your handling code here: 
        final String From = txtFrom.getText();
        final String password = txtpassword.getText();
        String To = txtTo.getText();
        String subject = jTextField3.getText();
        String txtmessage = jTextArea1.getText();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");//SSL protocol port no 465
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(From, password);
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(From));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(To));
            message.setSubject(subject);
            //message.setText(txtmessage);
            //code for set text message
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(txtmessage);
            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(messageBodyPart);

            //code for attach file
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(txtDocAttach.getText());
            multiPart.addBodyPart(messageBodyPart);

            message.setContent(multiPart);

            Transport.send(message);
            JOptionPane.showMessageDialog(rootPane, "Message is sent");
        } catch (MessagingException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonMailActionPerformed

    private void buttonEachBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEachBarActionPerformed
        // TODO add your handling code here:
        String strP1 = labelChartAge.getText();
        String strP2 = labelChartHeight.getText();
        String strP3 = labelChartWeight.getText();

        DefaultCategoryDataset catDataset = new DefaultCategoryDataset();
        catDataset.setValue(new Float(strP1), "Value", "Age");
        catDataset.setValue(new Float(strP2), "Value", "Height");
        catDataset.setValue(new Float(strP3), "Value", "Weight");

        //.createBarChart3D
        chart = ChartFactory.createBarChart("Bar Chart",
                "Parameters", "Values", catDataset, PlotOrientation.VERTICAL, false, true, false);
        //chart.setBackgroundPaint(Color.white);
        chart.getTitle().setPaint(Color.red);

        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.black);

        frame.setChart(chart);
        frame.setSize(400, 240);
        labelChart.setIcon(null);
        labelChart.setText(null);
        panelChart.setBackground(Color.white);
        panelChart.add(frame);
    }//GEN-LAST:event_buttonEachBarActionPerformed

    private void buttonEachPieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEachPieActionPerformed
        // TODO add your handling code here:
        String strP1 = labelChartAge.getText();
        String strP2 = labelChartHeight.getText();
        String strP3 = labelChartWeight.getText();

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Age", new Float(strP1));
        pieDataset.setValue("height", new Float(strP2));
        pieDataset.setValue("Weight", new Float(strP3));

        //pieChart 2D
        // JFreeChart chart = ChartFactory.createPieChart("Pie Chart", pieDataset, true, true, true);
        // PiePlot p = (PiePlot)chart.getPlot();
        //pieChart 3D
        chart = ChartFactory.createPieChart3D("Pie Chart", pieDataset, true, true, true);
        Plot p = chart.getPlot();

        frame.setChart(chart);
        frame.setSize(400, 240);
        labelChart.setIcon(null);
        labelChart.setText(null);
        panelChart.setBackground(Color.white);
        panelChart.add(frame);
    }//GEN-LAST:event_buttonEachPieActionPerformed

    private void buttonEachLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEachLineActionPerformed
        // TODO add your handling code here:
        String strP1 = labelChartAge.getText();
        String strP2 = labelChartHeight.getText();
        String strP3 = labelChartWeight.getText();

        DefaultCategoryDataset catDataset = new DefaultCategoryDataset();
        catDataset.setValue(new Float(strP1), "Value", "Age");
        catDataset.setValue(new Float(strP2), "Value", "Height");
        catDataset.setValue(new Float(strP3), "Value", "Weight");

        //createLineChart3D
        chart = ChartFactory.createLineChart("Line Chart",
                "Parameters", "Values", catDataset, PlotOrientation.VERTICAL, false, true, false);
        //chart.setBackgroundPaint(Color.orange);
        chart.getTitle().setPaint(Color.red);

        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.black);

        frame.setChart(chart);
        frame.setSize(400, 240);
        labelChart.setIcon(null);
        labelChart.setText(null);
        panelChart.setBackground(Color.white);
        panelChart.add(frame);
    }//GEN-LAST:event_buttonEachLineActionPerformed

    private void buttonOvrallBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOvrallBarActionPerformed
        // TODO add your handling code here:
        String tmp = (String) comboChart.getSelectedItem();
        try {
            String query = "select Student_id," + tmp + " from Student_data";
            JDBCCategoryDataset dataset = new JDBCCategoryDataset(javaConnect.connectDb(), query);
            chart = ChartFactory.createBarChart("query Chart",
                    "Student Id", tmp, dataset, PlotOrientation.VERTICAL, false, true, true);
            BarRenderer renderer = null;

            //CategoryPlot plot = null;
            //renderer = new BarRenderer();
            CategoryPlot plot = chart.getCategoryPlot();
            CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
            xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            renderer = new BarRenderer();

            frame.setChart(chart);
            frame.setSize(400, 240);
            labelChart.setIcon(null);
            labelChart.setText(null);
            panelChart.setBackground(Color.white);
            panelChart.add(frame);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonOvrallBarActionPerformed

    private void buttonOvrallLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOvrallLineActionPerformed
        // TODO add your handling code here:
        String tmp = (String) comboChart.getSelectedItem();
        try {
            String query = "select Student_id," + tmp + " from Student_data";
            JDBCCategoryDataset dataset = new JDBCCategoryDataset(javaConnect.connectDb(), query);
            chart = ChartFactory.createLineChart("query Chart",
                    "Student Id", tmp, dataset, PlotOrientation.VERTICAL, false, true, true);
            BarRenderer renderer = null;

            //CategoryPlot plot = null;
            //renderer = new BarRenderer();
            CategoryPlot plot = chart.getCategoryPlot();
            CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
            xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            renderer = new BarRenderer();

            frame.setChart(chart);
            frame.setSize(400, 240);
            labelChart.setIcon(null);
            labelChart.setText(null);
            panelChart.setBackground(Color.white);
            panelChart.add(frame);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonOvrallLineActionPerformed

    private void buttonCaptureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCaptureActionPerformed
        // TODO add your handling code here:
        try {
            final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            final File file1 = new File("Chart.png");
            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
            JOptionPane.showMessageDialog(rootPane, "Chart Image is Captured");
        } catch (IOException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonCaptureActionPerformed

    private void buttonReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReportActionPerformed
        // TODO add your handling code here:
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Report.pdf"));
            document.open();

            //barcode generator
            PdfContentByte CB = writer.getDirectContent();
            BarcodeEAN codeEAN = new BarcodeEAN();
            codeEAN.setCode("1234567891011");
            Paragraph para = new Paragraph();
            document.add(new Paragraph("Barcode UDCA"));
            codeEAN.setCodeType(Barcode.UPCA);
            codeEAN.setCode("1110987654321");
            document.add(codeEAN.createImageWithBarcode(CB, BaseColor.BLACK, BaseColor.BLACK));
            document.add(para);

            //add image
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("Files\\star.jpg");
            document.add(image);

            document.add(new Paragraph("Student Health Report", FontFactory.getFont(
                    FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.RED)));
            document.add(new Paragraph(new Date().toString()));
        
            
            document.add(new Paragraph("***************************"
                    + "******************************************"
                    + "*******************************************"));

            //add table
           
            document.add(new Paragraph(" "));
            document.add(new Chunk("Data Table : ", FontFactory.getFont(
                    FontFactory.TIMES_BOLD, 16, Font.UNDERLINE, BaseColor.BLUE)));
            PdfPTable table = new PdfPTable(2);

            PdfPCell cell = new PdfPCell(new Paragraph("Report"));
            cell.setColspan(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.GREEN);
            cell.setPadding(10.0f);
            table.addCell(cell);

            table.addCell("Student Id");
            table.addCell(txtStId.getText());
            table.addCell("First Name");
            table.addCell(txtStFName.getText());
            table.addCell("Last Name");
            table.addCell(txtStLName.getText());
            table.addCell("Department");
            table.addCell(txtStDept.getText());
            table.addCell("Series");
            table.addCell(txtStContact.getText());
            table.addCell("Age");
            table.addCell(txtStAge.getText());
            table.addCell("Height");
            table.addCell(txtStHeight.getText());
            table.addCell("Weight");
            table.addCell(txtStWeight.getText());
            table.addCell("Gender");
            table.addCell((String) comboGender.getSelectedItem());
            table.addCell("Blood Group");
            table.addCell(txtStBlood.getText());
            document.add(table);

            /*
             //add list
             com.itextpdf.text.List list = new com.itextpdf.text.List(true, 100);
             list.add("First item");
             list.add("second item");
             list.add("Third item");
             list.add("Fourth item");
             document.add(list
             */
            document.add(new Paragraph(" "));
            document.add(new Chunk("Health Status : ", FontFactory.getFont(
                    FontFactory.TIMES_BOLD, 16, Font.NORMAL, BaseColor.BLUE)));
            document.add(new Chunk(status));

            document.add(new Paragraph(" "));
            document.add(new Chunk("Comment :", FontFactory.getFont(
                    FontFactory.TIMES_BOLD, 16, Font.UNDERLINE, BaseColor.BLUE)));
            document.add(new Chunk(tAreaComment.getText()));

            if (("checked".equals(decision))) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Chart :", FontFactory.getFont(
                        FontFactory.TIMES_BOLD, 16, Font.UNDERLINE, BaseColor.BLUE)));
                com.itextpdf.text.Image imgChart = com.itextpdf.text.Image.getInstance("Chart.png");
                //imgChart.setRotation(45.0f);
                imgChart.scaleAbsolute(520, 300);
                document.add(imgChart);
            }

            document.add(new Paragraph(" "));

            document.close();
            JOptionPane.showMessageDialog(rootPane, "Report is saved.");
        } catch (DocumentException | IOException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonReportActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        decision = "checked";
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        status = "Not Good. Need to be careful! ";
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        status = "Good. No need action. ";
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        decision = "Not ckecked";
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void buttonDocAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDocAttachActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(rootPane);
        File f = chooser.getSelectedFile();
        //File f= chooser.getCurrentDirectory();
        String filePath = f.getAbsolutePath();
        txtDocAttach1.setText(filePath);
    }//GEN-LAST:event_buttonDocAttachActionPerformed

    private void buttonDocAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDocAddActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "insert into Document_table(Doc_id,Student_id,Doc_name,Path)"
                    + "values(?,?,?,?) ";
            pst = connection.prepareStatement(sql);
            pst.setString(1, txtDocDocId.getText());
            pst.setString(2, txtDocStudentId.getText());
            pst.setString(3, txtDocDocName.getText());
            pst.setString(4, txtDocAttach1.getText());

            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
        updateDocTable();
    }//GEN-LAST:event_buttonDocAddActionPerformed

    private void buttonDocDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDocDelActionPerformed
        // TODO add your handling code here:
        String sql = "delete from Document_table where Doc_id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, txtDocDocId.getText());
            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Deleted");
            updateDocTable();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonDocDelActionPerformed

    private void tableDocumentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDocumentMouseClicked
        // TODO add your handling code here
        try {
            int row = tableDocument.getSelectedRow();
            String tableClick = (tableDocument.getModel().getValueAt(row, 0).toString());
            String sql = "select * from Document_table where Doc_id= '" + tableClick + "' ";
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                getDocData();
            }
        } catch (SQLException | ParseException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }

        int row = tableDocument.getSelectedRow();
        String value = (tableDocument.getModel().getValueAt(row, 3).toString());
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error");
        }
    }//GEN-LAST:event_tableDocumentMouseClicked

    private void buttonDocClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDocClearActionPerformed
        // TODO add your handling code here:
        txtDocAttach1.setText(null);
        txtDocDocId.setText(null);
        txtDocStudentId.setText(null);
        txtDocDocName.setText(null);
    }//GEN-LAST:event_buttonDocClearActionPerformed

    private void buttonEachOverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEachOverActionPerformed
        // TODO add your handling code here:
        String strP1 = labelChartAge.getText();
        String strP2 = labelChartHeight.getText();
        String strP3 = labelChartWeight.getText();

        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        dataset1.setValue(new Float(strP1), "Value", "Age");
        dataset1.setValue(new Float(strP2), "Value", "Height");
        dataset1.setValue(new Float(strP3), "Value", "Weight");

        final CategoryItemRenderer renderer = new BarRenderer();
        renderer.setItemLabelsVisible(true);

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);
        plot.setDomainAxis(new CategoryAxis("Category"));
        plot.setRangeAxis(new NumberAxis("Value"));
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        //2nd
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        dataset2.setValue(new Float(strP1), "Value", "Age");
        dataset2.setValue(new Float(strP2), "Value", "Height");
        dataset2.setValue(new Float(strP3), "Value", "Weight");

        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);
        final ValueAxis rangeAxis2 = new NumberAxis("Axis 2");
        plot.setRangeAxis(1, rangeAxis2);

        plot.mapDatasetToRangeAxis(2, 1);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Overlaid Bar Chart");

        frame.setChart(chart);
        frame.setSize(400, 240);
        labelChart.setIcon(null);
        labelChart.setText(null);
        panelChart.setBackground(Color.white);
        panelChart.add(frame);


    }//GEN-LAST:event_buttonEachOverActionPerformed

    private void buttonOvrallOverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOvrallOverActionPerformed
        // TODO add your handling code here:
        String tmp = (String) comboChart.getSelectedItem();
        try {
            String query = "select Student_id," + tmp + " from Student_data";
            JDBCCategoryDataset dataset1 = new JDBCCategoryDataset(javaConnect.connectDb(), query);

            final CategoryItemRenderer renderer = new BarRenderer();
            renderer.setItemLabelsVisible(true);

            final CategoryPlot plot = new CategoryPlot();
            plot.setDataset(dataset1);
            plot.setRenderer(renderer);
            plot.setDomainAxis(new CategoryAxis("Category"));
            plot.setRangeAxis(new NumberAxis("Value"));
            plot.setOrientation(PlotOrientation.VERTICAL);
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);

            //2nd
            String tmp2 = (String) comboChart.getSelectedItem();
            String query2 = "select Student_id," + tmp2 + " from Student_data";
            JDBCCategoryDataset dataset2 = new JDBCCategoryDataset(javaConnect.connectDb(), query2);
            final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
            plot.setDataset(1, dataset2);
            plot.setRenderer(1, renderer2);
            final ValueAxis rangeAxis2 = new NumberAxis("Axis 2");
            plot.setRangeAxis(1, rangeAxis2);

            plot.mapDatasetToRangeAxis(2, 1);
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
            plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            final JFreeChart chart = new JFreeChart(plot);
            chart.setTitle("Overlaid Bar Chart");

            frame.setChart(chart);
            frame.setSize(400, 240);
            labelChart.setIcon(null);
            labelChart.setText(null);
            panelChart.setBackground(Color.white);
            panelChart.add(frame);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_buttonOvrallOverActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }

                //UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
                // UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                //UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                //UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Stat_Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Stat_Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Stat_Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Stat_Jframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Stat_Jframe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Jmenu5;
    private javax.swing.JButton buttonAbout;
    private javax.swing.JButton buttonAbout1;
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonAttach;
    private javax.swing.JButton buttonCapture;
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonDocAdd;
    private javax.swing.JButton buttonDocAttach;
    private javax.swing.JButton buttonDocClear;
    private javax.swing.JButton buttonDocDel;
    private javax.swing.JButton buttonEachBar;
    private javax.swing.JButton buttonEachLine;
    private javax.swing.JButton buttonEachOver;
    private javax.swing.JButton buttonEachPie;
    private javax.swing.JButton buttonEdit;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton buttonHelp;
    private javax.swing.JButton buttonImgSave;
    private javax.swing.JButton buttonMail;
    private javax.swing.JButton buttonOvrallBar;
    private javax.swing.JButton buttonOvrallLine;
    private javax.swing.JButton buttonOvrallOver;
    private javax.swing.JButton buttonReport;
    private javax.swing.JButton buttonUpload;
    private javax.swing.JComboBox comboChart;
    private javax.swing.JComboBox comboGender;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelChart;
    private javax.swing.JLabel labelDeviation;
    private javax.swing.JLabel labelImage;
    private javax.swing.JLabel labelTotalFemale;
    private javax.swing.JLabel labelTotalMale;
    private javax.swing.JLabel labelTotalStudent;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenu menuDate;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuHelp;
    private javax.swing.JMenuItem menuNew;
    private javax.swing.JMenu menuTime;
    private javax.swing.JPanel panelChart;
    private javax.swing.JPanel panelStInfo;
    private javax.swing.JSlider sliderAge;
    private javax.swing.JSlider sliderHeight;
    private javax.swing.JSlider sliderWeight;
    private javax.swing.JTextArea tAreaComment;
    private javax.swing.JTable tableDocument;
    private javax.swing.JTable tableIdentify;
    private javax.swing.JTable tableStudentData;
    private javax.swing.JTextField txtAMeanEach;
    private javax.swing.JTextField txtAMeanFemale;
    private javax.swing.JTextField txtAMeanMale;
    private javax.swing.JTextField txtAMeanOver;
    private javax.swing.JTextField txtAMediEach;
    private javax.swing.JTextField txtAMediFemale;
    private javax.swing.JTextField txtAMediMale;
    private javax.swing.JTextField txtAMediOver;
    private javax.swing.JTextField txtAModeEach;
    private javax.swing.JTextField txtAModeFemale;
    private javax.swing.JTextField txtAModeMale;
    private javax.swing.JTextField txtAModeOver;
    private javax.swing.JTextField txtAttachName;
    private javax.swing.JTextField txtDocAttach;
    private javax.swing.JTextField txtDocAttach1;
    private javax.swing.JTextField txtDocDocId;
    private javax.swing.JTextField txtDocDocName;
    private javax.swing.JTextField txtDocStudentId;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtHMeanEach;
    private javax.swing.JTextField txtHMeanFemale;
    private javax.swing.JTextField txtHMeanMale;
    private javax.swing.JTextField txtHMeanOver;
    private javax.swing.JTextField txtHMediEach;
    private javax.swing.JTextField txtHMediFemale;
    private javax.swing.JTextField txtHMediMale;
    private javax.swing.JTextField txtHMediOver;
    private javax.swing.JTextField txtHModeEach;
    private javax.swing.JTextField txtHModeFemale;
    private javax.swing.JTextField txtHModeMale;
    private javax.swing.JTextField txtHModeOver;
    private javax.swing.JTextField txtImgPath;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStAge;
    private javax.swing.JTextField txtStBlood;
    private javax.swing.JTextField txtStContact;
    private javax.swing.JTextField txtStDept;
    private javax.swing.JTextField txtStFName;
    private javax.swing.JTextField txtStHeight;
    private javax.swing.JTextField txtStId;
    private javax.swing.JTextField txtStLName;
    private javax.swing.JTextField txtStWeight;
    private javax.swing.JTextField txtTo;
    private javax.swing.JTextField txtWMeanEach;
    private javax.swing.JTextField txtWMeanFemale;
    private javax.swing.JTextField txtWMeanMale;
    private javax.swing.JTextField txtWMeanOver;
    private javax.swing.JTextField txtWMediEach;
    private javax.swing.JTextField txtWMediFemale;
    private javax.swing.JTextField txtWMediMale;
    private javax.swing.JTextField txtWMediOver;
    private javax.swing.JTextField txtWModeEach;
    private javax.swing.JTextField txtWModeFemale;
    private javax.swing.JTextField txtWModeMale;
    private javax.swing.JTextField txtWModeOver;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JMenuItem webHelp;
    // End of variables declaration//GEN-END:variables
    private ImageIcon format = null;
    private String fileName;
    byte[] personImg = null;

    private String filePath;

    //my variables for chart
    private final JLabel labelChartAge = new JLabel();
    private final JLabel labelChartHeight = new JLabel();
    private final JLabel labelChartWeight = new JLabel();
    private final ChartPanel frame = new ChartPanel(null);
    private JFreeChart chart;

    //variable declaration for report
    private String status;
    private String decision;

}
