package mkl.testarea.itext7.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.DocumentRenderer;

/**
 * Experiments with document layout structures.
 * 
 * @author mkl
 */
public class FunnyDocumentLayouts
{
    final static File RESULT_FOLDER = new File("target/test-outputs", "content");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        RESULT_FOLDER.mkdirs();
    }

    /**
     * Render into a diagonal sequence of 100x100 rectangles.
     */
    @Test
    public void testManyColummns() throws FileNotFoundException
    {
        FileOutputStream fos = new FileOutputStream(new File(RESULT_FOLDER, "funnyDocumentLayouts.pdf"));
        PdfWriter writer = new PdfWriter(fos);
        
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.setRenderer(new MultiColumnDocumentRenderer(doc, new Rectangle(50, 700, 100, 100),
                new Rectangle(150, 600, 100, 100), new Rectangle(250, 500, 100, 100),
                new Rectangle(350, 400, 100, 100), new Rectangle(450, 300, 100, 100)));

        StringBuilder builder = new StringBuilder();
        builder.append(0);
        for (int i = 1; i < 1000; i++)
        {
            builder.append(' ').append(i);
        }
        
        doc.add(new Paragraph(builder.toString()).setBackgroundColor(ColorConstants.YELLOW));
        doc.close();
    }

    /**
     * Render into a document with a pagesize not having the origin at the lower left.
     */
    @Test
    public void testNonOriginMediaBox() throws FileNotFoundException
    {
        FileOutputStream fos = new FileOutputStream(new File(RESULT_FOLDER, "nonOriginMediaBox.pdf"));
        PdfWriter writer = new PdfWriter(fos);
        
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(new Rectangle(595, 842, 595, 842)));

        StringBuilder builder = new StringBuilder();
        builder.append(0);
        for (int i = 1; i < 1000; i++)
        {
            builder.append(' ').append(i);
        }
        
        doc.add(new Paragraph(builder.toString()).setBackgroundColor(ColorConstants.YELLOW));
        doc.close();
    }

    @Ignore
    @Test
    public void testMultipleRenderers() throws FileNotFoundException
    {
        FileOutputStream fos = new FileOutputStream(new File(RESULT_FOLDER, "multipleRenderers.pdf"));
        PdfWriter writer = new PdfWriter(fos);
        
        PdfDocument pdfDoc = new PdfDocument(writer);


        while (pdfDoc.getNumberOfPages() < 10) {
            Document doc = new Document(pdfDoc);
            doc.setRenderer(new DocumentRenderer(doc));
            for (int i = 0; i < 15; i++)
                doc.add(new Paragraph("THIS IS NORMAL TEXT"));
            doc.flush();

            doc = new Document(pdfDoc);
            Rectangle effectiveArea = doc.getPageEffectiveArea(pdfDoc.getDefaultPageSize());
            doc.setRenderer(new ColumnDocumentRenderer(doc, new Rectangle[]{
                    effectiveArea.clone().setWidth(effectiveArea.getWidth() * 0.45f),
                    effectiveArea.clone().setWidth(effectiveArea.getWidth() * 0.45f).setX(effectiveArea.getX() + effectiveArea.getWidth() * 0.55f)
            }));
            for (int i = 0; i < 30; i++)
                doc.add(new Paragraph("THIS IS COLUMN TEXT"));
            doc.flush();
        }

        pdfDoc.close();
    }
}
