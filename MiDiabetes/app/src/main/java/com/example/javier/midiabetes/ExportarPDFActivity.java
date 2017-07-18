package com.example.javier.midiabetes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class ExportarPDFActivity extends Fragment {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private CheckBox checkGlucosa, checkComidas, checkHemoglobina, checkTabla, checkEstadisticas, checkGrafica;
    private DatePicker datePicker;
    private Calendar fecha1, fecha2;
    private SimpleDateFormat sdf;
    private Button btnfecha2, btnfecha1;
    private LineChart lineChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_exportar_pdf, container, false);

        sdf = new SimpleDateFormat("dd/MM/yyyy");

        lineChart = (LineChart) view.findViewById(R.id.pieChartPDF);

        checkGlucosa = (CheckBox) view.findViewById(R.id.checkBoxGlucosa);
        checkComidas = (CheckBox) view.findViewById(R.id.checkBoxComidas);
        checkHemoglobina = (CheckBox) view.findViewById(R.id.checkBoxHba1c);
        checkTabla = (CheckBox) view.findViewById(R.id.checkBoxTablas);
        checkEstadisticas = (CheckBox) view.findViewById(R.id.checkBoxEstadisticas);
        checkGrafica = (CheckBox) view.findViewById(R.id.checkBoxGraficas);

        datePicker = new DatePicker(getContext());

        btnfecha1 = (Button) view.findViewById(R.id.btnFechaPDF);
        btnfecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(datePicker)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(datePicker.getParent()!=null)
                                    ((ViewGroup)datePicker.getParent()).removeView(datePicker);

                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth() + 1;
                                int year = datePicker.getYear();

                                String fecha = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);

                                fecha1 = Calendar.getInstance();
                                try {
                                    fecha1.setTime(sdf.parse(fecha));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                btnfecha1.setText(fecha);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(datePicker.getParent()!=null)
                                    ((ViewGroup)datePicker.getParent()).removeView(datePicker);
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        btnfecha2 = (Button) view.findViewById(R.id.btnFecha2PDf);
        btnfecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(datePicker)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(datePicker.getParent()!=null)
                                    ((ViewGroup)datePicker.getParent()).removeView(datePicker);

                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth() + 1;
                                int year = datePicker.getYear();

                                String fecha = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);

                                fecha2 = Calendar.getInstance();

                                try {
                                    fecha2.setTime(sdf.parse(fecha));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                btnfecha2.setText(fecha);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(datePicker.getParent()!=null)
                                    ((ViewGroup)datePicker.getParent()).removeView(datePicker);
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        Button btnexportar = (Button) view.findViewById(R.id.btnExportarPDF);
        btnexportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPDF();
            }
        });

    return view;
    }

    public void crearPDF(){
        try {
            Document document = new Document();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + "MiDiabetes.pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            if(checkGlucosa.isChecked()){
                addContentGlucosa(document);
            }
            if(checkComidas.isChecked()){
                addContentComidas(document);
            }
            if(checkHemoglobina.isChecked()){
                addContentHbA1c(document);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast toast1 = Toast.makeText(getContext().getApplicationContext(), "Documento creado en sdCard/MiDiabetes.pdf" , Toast.LENGTH_SHORT);
        toast1.show();
    }

    private void addMetaData(Document document) {
        document.addTitle("Datos diabetologicos de MiDiabetes");
    }

    private void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("Datos diabetologicos del paciente desde el día "+btnfecha1.getText()+" hasta el día "+btnfecha2.getText(), catFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "Documento creado el día: " + Calendar.DAY_OF_MONTH +"/"+ Calendar.MONTH +"/"+ Calendar.YEAR,
                smallBold));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private void addContentGlucosa(Document document) throws DocumentException, IOException, ParseException {
        Paragraph parrafo1 = new Paragraph();

        parrafo1.add(new Paragraph("Historial de glucosa:", catFont));
        if(checkTabla.isChecked()){
            parrafo1.add(new Phrase("En la siguiente tabla se muestra los datos sobre los niveles del glucosa del paciente:"));
            addEmptyLine(parrafo1, 1);
            createTableGlucosa(parrafo1);
            document.add(parrafo1);
        }
        if(checkGrafica.isChecked()){
            Paragraph parrafo3 = new Paragraph();
            addEmptyLine(parrafo3, 1);
            parrafo3.add(new Phrase("GRAFICA:"));
            addEmptyLine(parrafo3, 1);
            createGraficaGlucosa();
            String pathimage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "GraficaGlucosa.jpg";
            Image image1 = Image.getInstance(pathimage);
            image1.scaleAbsolute(250f, 250f);
            parrafo3.add(image1);
            document.add(parrafo3);
        }
       if(checkEstadisticas.isChecked()){
           Paragraph parrafo4 = new Paragraph();
           addEmptyLine(parrafo4, 1);
           parrafo4.add(new Phrase("ESTADISTICAS:"));
           addEmptyLine(parrafo4, 1);
           createEstadisticasGlucosa(parrafo4);
           document.add(parrafo4);
       }
        document.newPage();
    }

    private void addContentComidas(Document document) throws DocumentException, IOException, ParseException {
        Paragraph parrafo1 = new Paragraph();
        addEmptyLine(parrafo1, 1);
        parrafo1.add(new Paragraph("Historial de hidratos ingeridos:", catFont));
        if(checkTabla.isChecked()){
            parrafo1.add(new Phrase("En la siguiente tabla se muestran los hidratos consumidos:"));
            addEmptyLine(parrafo1, 1);
            createTableComidas(parrafo1);
            document.add(parrafo1);
        }
        if(checkGrafica.isChecked()){
            Paragraph parrafo3 = new Paragraph();
            addEmptyLine(parrafo3, 1);
            parrafo3.add(new Phrase("GRAFICA:"));
            addEmptyLine(parrafo3, 1);
            createGraficaComidas();
            String pathimage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "GraficaComidas.jpg";
            Image image1 = Image.getInstance(pathimage);
            image1.scaleAbsolute(250f, 250f);
            parrafo3.add(image1);
            document.add(parrafo3);
        }
        if(checkEstadisticas.isChecked()){
            Paragraph parrafo4 = new Paragraph();
            addEmptyLine(parrafo4, 1);
            parrafo4.add(new Phrase("ESTADISTICAS:"));
            addEmptyLine(parrafo4, 1);
            createEstadisticasComidas(parrafo4);
            document.add(parrafo4);
        }
        document.newPage();
    }

    private void addContentHbA1c(Document document) throws DocumentException, IOException, ParseException {
        Paragraph parrafo1 = new Paragraph();
        addEmptyLine(parrafo1, 1);
        parrafo1.add(new Paragraph("Historial de la hemoglobina glicosilada (HbA1c):", catFont));
        if(checkTabla.isChecked()){
            parrafo1.add(new Phrase("En la siguiente tabla se muestran el historial de valores de la hemoglobina glicosilada:"));
            addEmptyLine(parrafo1, 1);
            createTableHbA1c(parrafo1);
            document.add(parrafo1);
        }
        if(checkGrafica.isChecked()){
            Paragraph parrafo3 = new Paragraph();
            addEmptyLine(parrafo3, 1);
            parrafo3.add(new Phrase("GRAFICA:"));
            addEmptyLine(parrafo3, 1);
            createGraficaHbA1c();
            String pathimage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "GraficaHemoglobina.jpg";
            Image image1 = Image.getInstance(pathimage);
            image1.scaleAbsolute(250f, 250f);
            parrafo3.add(image1);
            parrafo3.add(Chunk.TABBING);
            document.add(parrafo3);
        }
        if(checkEstadisticas.isChecked()){
            Paragraph parrafo4 = new Paragraph();
            addEmptyLine(parrafo4, 1);
            parrafo4.add(new Phrase("ESTADISTICAS:"));
            addEmptyLine(parrafo4, 1);
            createEstadisticasHbA1c(parrafo4);
            document.add(parrafo4);
        }
        document.newPage();
    }

    public void createTableGlucosa(Paragraph parrafo2) throws DocumentException, ParseException {
        PdfPTable table = new PdfPTable(6);
        DataBaseManagerGlucosa managerGlucosa = new DataBaseManagerGlucosa(getContext());
        Cursor cursor = managerGlucosa.cargarCursorGlucosa();
        PdfPCell c1, c2, c3, c4, c5, c6;
        c1 = new PdfPCell(new Phrase("Glucosa"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);
        c2 = new PdfPCell(new Phrase("Fecha"));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c2);
        c3 = new PdfPCell(new Phrase("Hora"));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c3);
        c4 = new PdfPCell(new Phrase("Antes/Después"));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c4);
        c5 = new PdfPCell(new Phrase("Momento del dia"));
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        c5.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c5);
        c6 = new PdfPCell(new Phrase("Comentario"));
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        c6.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c6);
        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                String fecha = cursor.getString(1);

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha1.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha1.get(fecha1.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha2.YEAR) && cal.get(cal.MONTH) == fecha2.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha2.get(fecha1.DAY_OF_MONTH)))){
                    c1 = new PdfPCell(new Phrase(cursor.getString(5)));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);
                    c2 = new PdfPCell(new Phrase(cursor.getString(1)));
                    c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c2);
                    c3 = new PdfPCell(new Phrase(cursor.getString(2)));
                    c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c3);
                    c4 = new PdfPCell(new Phrase(cursor.getString(3)));
                    c4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c4);
                    c5 = new PdfPCell(new Phrase(cursor.getString(4)));
                    c5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c5);
                    c6 = new PdfPCell(new Phrase(cursor.getString(6)));
                    c6.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c6);
                }

            }  while (cursor.moveToNext());
            cursor.close();
        }

        float[] columnWidths = {0.35f, 0.40f, 0.30f, 0.50f, 0.65f, 1f};
        table.setWidths(columnWidths);
        table.setWidthPercentage(110);
        table.setSpacingBefore (0f);
        table.setSpacingAfter (0f);
        parrafo2.add(table);
    }

    private void createTableComidas(Paragraph parrafo2) throws DocumentException, ParseException {
        PdfPTable table = new PdfPTable(6);
        DataBaseManagerNutricion managerNutricion = new DataBaseManagerNutricion(getContext());
        Cursor cursor = managerNutricion.cargarCursorNutricion();
        PdfPCell c1, c2, c3, c4, c5, c6;
        c1 = new PdfPCell(new Phrase("Comida"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);
        c2 = new PdfPCell(new Phrase("Fecha"));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c2);
        c3 = new PdfPCell(new Phrase("Peso"));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c3);
        c4 = new PdfPCell(new Phrase("Hidratos"));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c4);
        c5 = new PdfPCell(new Phrase("Raciones"));
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        c5.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c5);
        c6 = new PdfPCell(new Phrase("Ingredientes"));
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        c6.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c6);
        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                String fecha = cursor.getString(6);

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha1.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha1.get(fecha1.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha2.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha2.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha2.get(fecha1.DAY_OF_MONTH)))){
                    c1 = new PdfPCell(new Phrase(cursor.getString(1)));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);
                    c2 = new PdfPCell(new Phrase(cursor.getString(6)));
                    c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c2);
                    c3 = new PdfPCell(new Phrase(cursor.getString(2)));
                    c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c3);
                    c4 = new PdfPCell(new Phrase(cursor.getString(3)));
                    c4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c4);
                    c5 = new PdfPCell(new Phrase(cursor.getString(4)));
                    c5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c5);
                    c6 = new PdfPCell(new Phrase(cursor.getString(5)));
                    c6.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c6);
                }


            }  while (cursor.moveToNext());
            cursor.close();
        }

        float[] columnWidths = {0.60f, 0.50f, 0.35f, 0.35f, 0.30f, 1f};
        table.setWidths(columnWidths);
        table.setWidthPercentage(110);
        table.setSpacingBefore (0f);
        table.setSpacingAfter (0f);
        parrafo2.add(table);
    }

    private void createTableHbA1c(Paragraph parrafo2) throws DocumentException, ParseException {
        PdfPTable table = new PdfPTable(2);
        DataBaseManagerHemoglobina managerHemoglobina = new DataBaseManagerHemoglobina(getContext());
        Cursor cursor = managerHemoglobina.cargarCursorHemoglobina();
        PdfPCell c1, c2;
        c1 = new PdfPCell(new Phrase("Hemoglobina"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);
        c2 = new PdfPCell(new Phrase("Fecha"));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c2);
        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                String fecha = cursor.getString(2);

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha1.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha1.get(fecha1.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha2.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha2.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha2.get(fecha1.DAY_OF_MONTH)))){
                    c1 = new PdfPCell(new Phrase(cursor.getString(1)));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);
                    c2 = new PdfPCell(new Phrase(cursor.getString(2)));
                    c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c2);
                }
            }  while (cursor.moveToNext());
            cursor.close();
        }

        float[] columnWidths = {0.50f, 0.40f};
        table.setWidths(columnWidths);
        table.setWidthPercentage(110);
        table.setSpacingBefore (0f);
        table.setSpacingAfter (0f);
        parrafo2.add(table);
    }

    private void createEstadisticasGlucosa(Paragraph parrafo4) throws ParseException, DocumentException {
        PdfPTable table = new PdfPTable(5);
        DataBaseManagerGlucosa managerGlucosa = new DataBaseManagerGlucosa(getContext());
        Cursor cursor = managerGlucosa.cargarCursorGlucosa();
        PdfPCell c0, c1, c2, c3, c4;
        c0 = new PdfPCell(new Phrase("Controles realizados"));
        c0.setHorizontalAlignment(Element.ALIGN_CENTER);
        c0.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c0);
        c1 = new PdfPCell(new Phrase("Media"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);
        c2 = new PdfPCell(new Phrase("Mínima"));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c2);
        c3 = new PdfPCell(new Phrase("Máxima"));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c3);
        c4 = new PdfPCell(new Phrase("Estimación de HbA1c"));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c4);

        int i = 0;
        int glucosaMayor = 0, glucosa, glucosaMenor = Integer.MAX_VALUE;
        double sumaGlucosas=0;
        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                String fecha = cursor.getString(1);
                glucosa = Integer.parseInt(cursor.getString(5));

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha1.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha1.get(fecha1.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha2.YEAR) && cal.get(cal.MONTH) == fecha2.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha2.get(fecha1.DAY_OF_MONTH)))){
                    sumaGlucosas += glucosa;
                    if (glucosa > glucosaMayor) {
                        glucosaMayor = glucosa;
                    }
                    if (glucosa < glucosaMenor) {
                        glucosaMenor = glucosa;
                    }
                    i++;
                }

            }  while (cursor.moveToNext());
            cursor.close();
        }

        double HbA1c= ((sumaGlucosas/i) + 46.7) /28.7;

        c0 = new PdfPCell(new Phrase(String.valueOf(i)));
        c0.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c0);
        c1 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaGlucosas / i))));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c2 = new PdfPCell(new Phrase(String.valueOf(glucosaMenor)));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c2);
        c3 = new PdfPCell(new Phrase(String.valueOf(glucosaMayor)));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c3);
        c4 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", HbA1c))));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c4);
        parrafo4.add(table);
    }

    private void createEstadisticasComidas(Paragraph parrafo4) throws ParseException {
        PdfPTable table = new PdfPTable(7);

        DataBaseManagerNutricion managerNutricion = new DataBaseManagerNutricion(getContext());
        Cursor cursor = managerNutricion.cargarCursorNutricion();

        PdfPCell c0, c1, c2, c3, c4, c5, c6;
        c0 = new PdfPCell(new Phrase("Ingestas realizadas"));
        c0.setHorizontalAlignment(Element.ALIGN_CENTER);
        c0.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c0);
        c1 = new PdfPCell(new Phrase("Media de raciones"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);
        c2 = new PdfPCell(new Phrase("Raciones al día"));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c2);
        c3 = new PdfPCell(new Phrase("Raciones totales"));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c3);
        c4 = new PdfPCell(new Phrase("Media de hidratos"));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c4);
        c5 = new PdfPCell(new Phrase("Hidratos al día"));
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        c5.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c5);
        c6 = new PdfPCell(new Phrase("Hidratos totales"));
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        c6.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c6);

        int i = 0;
        String fecha;
        double raciones, hidratos, sumaHidratos = 0, sumaRaciones = 0;

        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                fecha = cursor.getString(6);
                hidratos = Double.valueOf(cursor.getString(3));
                raciones = Double.valueOf(cursor.getString(4));

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha1.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha1.get(fecha1.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha2.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha2.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha2.get(fecha1.DAY_OF_MONTH)))){
                    sumaHidratos += hidratos;
                    sumaRaciones += raciones;
                    i++;
                }

            }  while (cursor.moveToNext());
            cursor.close();
        }

        long different = fecha2.getTime().getTime() - fecha1.getTime().getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        c0 = new PdfPCell(new Phrase(String.valueOf(i)));
        c0.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c0);
        c1 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaRaciones / i))));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c2 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaRaciones/elapsedDays))));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c2);
        c3 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaRaciones))));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c3);
        c4 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaHidratos/i))));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c4);
        c5 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaHidratos/elapsedDays))));
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c5);
        c6 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaHidratos))));
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c6);

        table.setWidthPercentage(110);
        parrafo4.add(table);
    }

    private void createEstadisticasHbA1c(Paragraph parrafo) throws ParseException {
        PdfPTable table = new PdfPTable(2);
        DataBaseManagerHemoglobina managerHemoglobina = new DataBaseManagerHemoglobina(getContext());
        Cursor cursor = managerHemoglobina.cargarCursorHemoglobina();

        PdfPCell c0, c1;
        c0 = new PdfPCell(new Phrase("Controles realizados"));
        c0.setHorizontalAlignment(Element.ALIGN_CENTER);
        c0.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c0);
        c1 = new PdfPCell(new Phrase("Media de la Hemoglobina glicosilada"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);

        int i = 0;
        double sumaHbA1c=0;

        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                String fecha = cursor.getString(2);

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha1.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha1.get(fecha1.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha2.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha2.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha2.get(fecha1.DAY_OF_MONTH)))){
                    sumaHbA1c += Double.valueOf(cursor.getString(1));
                    i++;
                }
            }  while (cursor.moveToNext());
            cursor.close();
        }

        c0 = new PdfPCell(new Phrase(String.valueOf(i)));
        c0.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c0);
        c1 = new PdfPCell(new Phrase(String.valueOf(String.format("%.2f", sumaHbA1c / i))));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        parrafo.add(table);
    }

    private void createGraficaGlucosa() throws ParseException {
        Calendar cal = Calendar.getInstance();
        final HashMap<Integer, String> labels = new HashMap<>();
        labels.clear();

        DataBaseManagerGlucosa managerGlucosa = new DataBaseManagerGlucosa(getContext());

        final ArrayList<Entry> entries = new ArrayList<>();
        String fecha;
        final Cursor cursor = managerGlucosa.cargarCursorGlucosa();
        String fechaLabel2 = "";
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                fecha = cursor.getString(1);

                String[] separated = fecha.split("/");
                String fechaLabel = separated[0]+"/"+separated[1];

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == fecha1.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == fecha1.get(Calendar.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha1.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == fecha2.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == fecha2.get(Calendar.DAY_OF_MONTH)))){
                    entries.add(new Entry(i, Float.valueOf(cursor.getString(5))));
                    if(!fechaLabel.equals(fechaLabel2)){
                        labels.put(i,fechaLabel);
                        fechaLabel2=fechaLabel;
                    }else{
                        labels.put(i,"");
                    }
                    i++;
                }
            }while (cursor.moveToNext());
            cursor.close();
        }

        if(entries.size()>0) {

            LineDataSet dataset = new LineDataSet(entries, "Linea temporal de los niveles de glucosa");

            LineData data = new LineData(dataset);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setLabelCount(entries.size(), true);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                    if (entries.size() != 1) {
                        return labels.get((int) value);
                    } else return "";
                }
            });

            dataset.setDrawFilled(true);
            dataset.setFillColor(Color.YELLOW);
            dataset.setColors(Color.DKGRAY);
            dataset.setCircleColor(Color.BLACK);
            dataset.setCircleColorHole(Color.argb(255, 242, 165, 15));
            dataset.setCircleRadius(4);
            dataset.setValueTextSize(12);
            dataset.setValueTextColor(Color.BLACK);

            Description des = new Description();
            des.setText("Gráfica glucosa     ");

            lineChart.setDescription(des);
            lineChart.setData(data);
            lineChart.setVisibleXRange(entries.size()-1, entries.size()-1);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.moveViewToX(entries.get(entries.size()-1).getX());
            lineChart.invalidate();
        }
        Bitmap bitmap = lineChart.getChartBitmap();
        getImage("GraficaGlucosa", bitmap);
        lineChart.clear();
    }

    private void createGraficaComidas() throws ParseException {
        Calendar cal = Calendar.getInstance();
        final HashMap<Integer, String> labels = new HashMap<>();
        labels.clear();

        DataBaseManagerNutricion managerNutricion = new DataBaseManagerNutricion(getContext());

        final ArrayList<Entry> entries = new ArrayList<>();
        String fecha;
        String fechaLabel2 = "";
        final Cursor cursor = managerNutricion.cargarCursorNutricion();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                fecha = cursor.getString(6);

                String[] separated = fecha.split("/");
                String fechaLabel = separated[0]+"/"+separated[1];

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == fecha1.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == fecha1.get(Calendar.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha1.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == fecha2.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == fecha2.get(Calendar.DAY_OF_MONTH)))){
                    entries.add(new Entry(i, Float.valueOf(cursor.getString(3))));
                    if(!fechaLabel.equals(fechaLabel2)){
                        labels.put(i,fechaLabel);
                        fechaLabel2=fechaLabel;
                    }else{
                        labels.put(i,"");
                    }
                    i++;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        if(entries.size()>0) {

            LineDataSet dataset = new LineDataSet(entries, "Linea temporal de los hidratos consumidos");

            LineData data = new LineData(dataset);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setLabelCount(entries.size(), true);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                    if (entries.size() != 1) {
                        return labels.get((int) value);
                    } else return "";
                }
            });

            dataset.setDrawFilled(true);
            dataset.setFillColor(Color.YELLOW);
            dataset.setColors(Color.DKGRAY);
            dataset.setCircleColor(Color.BLACK);
            dataset.setCircleColorHole(Color.argb(255, 242, 165, 15));
            dataset.setCircleRadius(4);
            dataset.setValueTextSize(12);
            dataset.setValueTextColor(Color.BLACK);

            Description des = new Description();
            des.setText("Gráfica Hidratos     ");

            lineChart.setDescription(des);
            lineChart.setData(data);
            lineChart.setVisibleXRange(entries.size()-1, entries.size()-1);
            lineChart.moveViewToX(entries.get(entries.size()-1).getX());
            lineChart.invalidate();
        }
        Bitmap bitmap = lineChart.getChartBitmap();
        getImage("GraficaComidas", bitmap);
        lineChart.clear();
    }

    private void createGraficaHbA1c() throws ParseException {
        final ArrayList<Entry> entries = new ArrayList<>();
        DataBaseManagerHemoglobina managerHemoglobina = new DataBaseManagerHemoglobina(getContext());
        Cursor cursor = managerHemoglobina.cargarCursorHemoglobina();
        final HashMap<Integer, String> labels = new HashMap<>();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                Calendar cal = Calendar.getInstance();
                String fecha = cursor.getString(2);

                cal.setTime(sdf.parse(fecha));

                if ((fecha1.before(cal) || (cal.get(Calendar.YEAR) == fecha1.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha1.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha1.get(fecha1.DAY_OF_MONTH)))
                        && (fecha2.after(cal) || (cal.get(Calendar.YEAR) == fecha2.get(fecha1.YEAR) && cal.get(cal.MONTH) == fecha2.get(fecha1.MONTH) && cal.get(cal.DAY_OF_MONTH) == fecha2.get(fecha1.DAY_OF_MONTH)))){
                    entries.add(new Entry(i, Float.valueOf(cursor.getString(1))));
                    labels.put(i, cursor.getString(2));
                    i++;
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        if (entries.size() > 0) {

            LineDataSet dataset = new LineDataSet(entries, "Linea temporal de los niveles de la hemoglobina glicosilada");

            LineData data = new LineData(dataset);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setLabelCount(entries.size(), true);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                    if (entries.size() != 1) {
                        return labels.get((int) value);
                    } else return "";
                }
            });

            dataset.setDrawFilled(true);
            dataset.setFillColor(Color.YELLOW);
            dataset.setColors(Color.DKGRAY);
            dataset.setCircleColor(Color.BLACK);
            dataset.setCircleColorHole(Color.argb(255, 242, 165, 15));
            dataset.setCircleRadius(4);
            dataset.setValueTextSize(12);
            dataset.setValueTextColor(Color.BLACK);

            Description des = new Description();
            des.setText("Gráfica Hba1c");

            lineChart.setDescription(des);
            lineChart.setData(data);
            lineChart.setVisibleXRange(entries.size()-1, entries.size()-1);
            lineChart.moveViewToX(entries.get(entries.size()-1).getX());
            lineChart.invalidate();
        }
        Bitmap bitmap = lineChart.getChartBitmap();
        getImage("GraficaHemoglobina", bitmap);
        lineChart.clear();
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void getImage(String nombre, Bitmap linechart) {
        FileOutputStream fileOutputStream = null;

        String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +nombre+ ".jpg";

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        linechart.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream);

        try {
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}