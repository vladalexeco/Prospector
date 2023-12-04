package com.example.prospector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prospector.dialogs.QuitDialog;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class JournalActivity extends AppCompatActivity implements QuitDialog.QuitDialogListener, LocationListener {

    // константы и переменные для расчета расстояния до крайней отмеченной точки и для рассчета

    // общего расстояния пройденного пути
    final double pi = 3.1416; // число Пи
    final double radiusOfEarth = 6371.008; // радиус Земли, выраженный в километрах
    final static String checkCoord = "0" + "\u00BA" + "0" + "'" + "0.0" + "\""; // точка "по дефолту"

    static int pointDistance = 0;
    static int totalDistance; // общее пройденное расстояние при обследовании объекта, выраженное в метрах
    static String lastLat = checkCoord; // крайние по счету измеренные координаты. Нужны для рассчета
    static String lastLon = checkCoord; // расстояния до предыдущей точки
    //

    private static final int requestLocationCode = 1337;
    static final String lastOpenedFile = "lastOpenedFile.txt"; // имя файла в папке cache, где хранится имя последнего открытого журнала

    // Для получения параметров точки после прохода всех экранов
    // С учетом значения cancel (true или false)
    final static String cancel = "Cancel";
    final static String order = "Order";
    final static String search = "Search";
    final static String mad = "Mad";
    final static String index = "Index";
    final static String comment = "Comment";
    final static String latitude = "Latitude";
    final static String longitude = "Longitude";

    // Для получения данных из PassportActivity для формирования названия файла + метаданные (климатические параметры)
    final static String file = "File";
    final static String fileName = "File name";
    final static String name = "Name";
    final static String object = "Object";
    final static String numberOfObject = "NumberOfObject";
    final static String date = "Date";
    final static String climateSettings = "ClimateSettings";
    final static String temperature = "Temperature";
    final static String pressure = "Pressure";
    final static String humidity = "Humidity";
    final static String wind = "Wind";

    final static String editData = "editData";

    // Параметры климата, которые (в дальнейшем) будут фиксироваться в сохраняемом файле для последующего
    // вывода в Excel
    static String temperatureOnObj = null;
    static String pressureOnObj = null;
    static String humidityOnObj = null;
    static String windOnObj = null;

    static String nameOfFile = null; // Имя файла, под которым сохраняются данные

    int operation; // нужна, чтобы различать инструкции после вызова QuitDialog при удалении строки из RecyclerView.
                   // может принимать два значения 0 и 1
    int itemId; // при операции удаления c помощью контектстного диалога. Присваивает значение локальной переменной в функции обрабоки
                // запроса из контекстного меню (править/удалить). Затем используется в функции onQuitDialogPositiveClick

    TextView statusBar; // строка состояния внизу экрана
    TextView textViewNameOfFile; // виджет с именем файла
    TextView lastDistance;
    TextView textViewTotalDistance;

    RecyclerView recyclerView;
    static ArrayList<Point> points = new ArrayList<Point>();
    PointAdapter pointAdapter = new PointAdapter(points);
    static Integer pointCount = 0;

    static String formatLatitude = "no data";
    static String formatLongitude = "no data";

    static boolean inCache = false;

    static String currentDate = "unknown"; // текущая дата
    static String currentTime = "unknown"; // текущее время

    Date mDate;

    TextView round;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Intent intent = new Intent(JournalActivity.this, TopLevelActivity.class);
            startActivity(intent);
        }

        // Работа с темой
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean valueOfThemePreference = sharedPreferences.getBoolean("change_theme", false);

        if (valueOfThemePreference) {
            setTheme(R.style.Theme_AppCompat_Light);
        } else {
            setTheme(R.style.Theme_AppCompat);
        }
        //

        setContentView(R.layout.activity_journal);

        mDate = new Date();
        round = (TextView) findViewById(R.id.indicatorView);

        // Для определения GPS координат
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, requestLocationCode);
        }

        textViewNameOfFile = (TextView) findViewById(R.id.textViewNameOfFile); // Виджет, где прописано имя файла
        statusBar = (TextView) findViewById(R.id.status_bar); // Строка состояния внизу экрана
        lastDistance = (TextView) findViewById(R.id.textViewLastDistance);
        textViewTotalDistance = (TextView) findViewById(R.id.textViewTotalDistance);

        recyclerView = (RecyclerView) findViewById(R.id.point_recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pointAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            // Добавляет "слушателя" к recycleview, который реагирует на нажатия на items
            @Override
            public void onItemClick(View view, int position) {
                // Срабатывает при коротком нажатии на item
                Intent intent = new Intent(JournalActivity.this, PointInfoActivity.class);
                intent.putExtra(PointInfoActivity.serialOfPoint, position);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Срабатывает при длительном нажатии на item
//                Toast.makeText(JournalActivity.this, "You long click on " + position, Toast.LENGTH_SHORT).show();
            }
        }));

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            String file = arguments.getString(JournalActivity.file);
            if (file.equals("New file")) {
                // При открытии нового пустого журнала
                String name = arguments.getString(JournalActivity.name);
                String object = arguments.getString(JournalActivity.object);
                String numberOfObject = arguments.getString(JournalActivity.numberOfObject);
                String date = arguments.getString(JournalActivity.date);
                boolean climateSettings = arguments.getBoolean(JournalActivity.climateSettings);
                totalDistance = 0;

                if (climateSettings) {
                    temperatureOnObj = arguments.getString(JournalActivity.temperature);
                    pressureOnObj = arguments.getString(JournalActivity.pressure);
                    humidityOnObj = arguments.getString(JournalActivity.humidity);
                    windOnObj = arguments.getString(JournalActivity.wind);
                } else {
                    temperatureOnObj = pressureOnObj = humidityOnObj = windOnObj = "no data";
                }

                nameOfFile = object + "_" + numberOfObject + "_" + date + "_" + name;
                textViewNameOfFile.setText(nameOfFile);
                saveInCancheLastFileName(); // сохранение имени файла в кеше

            } else if (file.equals("Open file")) {
                // При открытии одного из ранее записанных журналов
                ArrayList<Point> tempPoints = new ArrayList<>();
                nameOfFile = arguments.getString(JournalActivity.fileName);
                textViewNameOfFile.setText(nameOfFile);
                String stringFile = openFile(nameOfFile);

                String[] arrStr = stringFile.split("\n");
                String pointString = "";
                for (int i = 0; i < arrStr.length - 2; i++) {
                    pointString += arrStr[i] + "\n";
                }

                String distance = arrStr[arrStr.length - 2];
                String climateOptions = arrStr[arrStr.length - 1];

                totalDistance = Integer.valueOf(distance);
                textViewTotalDistance.setText(String.valueOf(totalDistance));


                String[] arrClimateOptions = climateOptions.split(" ");
                temperatureOnObj = arrClimateOptions[0];
                pressureOnObj = arrClimateOptions[1];
                humidityOnObj = arrClimateOptions[2];
                windOnObj = arrClimateOptions[3];

                stringFile = pointString.substring(0, pointString.length() - 1); // Удаляем последний символ переноса в файле
                tempPoints = stringFileToPointsArrayList(stringFile);

                // сброс параметров таблицы
                points.clear();
                //

                points.addAll(tempPoints);
                pointAdapter.notifyItemRangeInserted(0, tempPoints.size());
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                pointCount += points.size();
                saveInCancheLastFileName(); // сохранение имени файла в кеше

                if (points.size() > 0) {
                    Point lastPoint = points.get(points.size() - 1);
                    if (lastPoint.getLatitude() != "no data" && lastPoint.getLongitude() != "no data") {
                        lastLat = lastPoint.getLatitude();
                        lastLon = lastPoint.getLongitude();
                    } else {
                        lastLat = checkCoord;
                        lastLon = checkCoord;
                    }

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Функция создает главное меню приложения
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_journal_activity, menu);
        return true;
    }

    public void onClickAddJournalBtn(View view) {
        // Срабатывает при нажатии клавиши "Добавить". Переводит в первый экран ввода данных.

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = dateFormat.format(mDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        currentTime = timeFormat.format(mDate);

        int num = points.size() + 1;

        JournalActivity.pointCount = num;
        Intent intent = new Intent(JournalActivity.this, IndexScreenActivity.class);
        intent.putExtra(IndexScreenActivity.order, JournalActivity.pointCount.toString());
        intent.putExtra(IndexScreenActivity.latitude, formatLatitude);
        intent.putExtra(IndexScreenActivity.longitude, formatLongitude);
        startActivity(intent);
    }

    public void onClickDelJournalBtn(View view) {
        // Срабатывает при нажатии клавиши удалить. Открывает диалог.
        if (points.size() > 0) {
            operation = 0;
            QuitDialog dialog = new QuitDialog("Удалить крайнюю точку?");
            dialog.show(getSupportFragmentManager(), "quit");
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        textViewNameOfFile.setText(nameOfFile);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            String file = arguments.getString(JournalActivity.file);
            if (file.equals("")) { // при формировании информации о точке (Проходятся по очеренди экраны)
                boolean canceled = arguments.getBoolean(JournalActivity.cancel);
                if (canceled) { // если при прохождении экранов была отменена точка
                    JournalActivity.pointCount--;
                } else { // если точка прошла все экраны
                    String pointOrder = arguments.getString(JournalActivity.order);
                    String pointSearch = arguments.getString(JournalActivity.search);
                    String pointMad = arguments.getString(JournalActivity.mad);
                    String pointIndex = arguments.getString(JournalActivity.index);
                    String pointComment = arguments.getString(JournalActivity.comment);
                    String latitude = arguments.getString(JournalActivity.latitude);
                    String longitude = arguments.getString(JournalActivity.longitude);

                    Point point = new Point(pointOrder, pointSearch, pointMad, pointIndex, pointComment, latitude, longitude, currentDate, currentTime);

                    if (!latitude.equals("no data")) {
                        lastLat = latitude;
                        lastLon = longitude;
                    } else {
                        lastLat = checkCoord;
                        lastLon = checkCoord;
                    }

                    lastDistance.setText("0 м");
                    totalDistance += pointDistance;
                    textViewTotalDistance.setText(String.valueOf(totalDistance + " м"));


                    if (points.size() > 0) {
                        Point prevPoint = points.get(points.size() - 1);

                        if (!prevPoint.getOrd().equals(point.getOrd())) {
                            points.add(point);
                            pointAdapter.notifyItemInserted(points.size() - 1);
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                        }
                    } else {
                        points.add(point);
                        pointAdapter.notifyItemInserted(points.size() - 1);
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }

                    //Работа с функцией сохранения точек после прохождения всех экранов
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    String valueSavePoints = sharedPreferences.getString("save_points", "");

                    if (TextUtils.isEmpty(valueSavePoints)) {
                        valueSavePoints = "5";
                    }

                    int valueSavePointsInt = Integer.valueOf(valueSavePoints);

                    if (valueSavePointsInt != 0) {
                        int result = points.size() % valueSavePointsInt;
                        if (result == 0) {
                            onClickSavePoints();
                        }
                    }

                }
            } else if (file.equals("fromEditScreen")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                currentDate = dateFormat.format(mDate);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                currentTime = timeFormat.format(mDate);

                String[] arrEdit = arguments.getStringArray(JournalActivity.editData);
                Point editedPoint = new Point(arrEdit[0], arrEdit[1], arrEdit[2], arrEdit[3], arrEdit[4], arrEdit[5], arrEdit[6], currentDate, currentTime);
                int ind = Integer.valueOf(arrEdit[0]) -1;
                points.set(ind, editedPoint);
                pointAdapter.notifyItemChanged(ind);
            }
        }
    }

    protected void onDestroy() {
        onClickSavePoints();
        super.onDestroy();
    }


    // Функции диалога, который открывается при нажатии кнопки удалить
    public void onQuitDialogPositiveClick(DialogFragment dialog) {
        if (operation == 0) {
            // Срабатывает при нажатии клавиши "Ок" диалога. Удаляет последнюю точку в списке
            JournalActivity.pointCount--;
            int lastIndex = points.size() - 1;
            points.remove(lastIndex);
            pointAdapter.notifyItemRemoved(lastIndex);
        } else if (operation == 1) {
            // при вызове контекстного меню и выборе функции удалить
            deletePointFromRecyclerView(itemId);
        } else if (operation == 2) {
            // при выходе в главное меню и сохранении файла
            onClickSavePoints();
            points.clear();
            pointCount = 0;
            Intent intent = new Intent(JournalActivity.this, TopLevelActivity.class);
            startActivity(intent);
        }
    }

    public void onQuitDialogNegativeClick(DialogFragment dialog) {
        // Ничего не делает
    }

    // Menu methods
    public void menuOnClickSavePoints(MenuItem menuItem) {
        // При нажатии на "Сохранить" в меню приложения сохраняет данные в файл с названием,
        // которое образуется из раннее введенных данных
        if (points.size() > 0) {
            onClickSavePoints();
        } else {
            Toast.makeText(this, "Пустой список", Toast.LENGTH_SHORT).show();
        }

    }

    public void menuOnClickExportToExcel(MenuItem menuItem) {
        // функция меню, экспортирующая данные в excel
        if (points.size() > 0) {
            exportToExcel();
            Toast.makeText(this, "Excel таблица создана", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Пустой список", Toast.LENGTH_SHORT).show();
        }

    }

    public void menuOnClickExportToKml(MenuItem menuItem) {
        if (points.size() > 0) {
            exportToKml();
            Toast.makeText(this, "XML файл создан", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Пустой список", Toast.LENGTH_SHORT).show();
        }

    }

    public void menuOnClickSendEmail(MenuItem menuItem) {
        if (points.size() > 0) {
            sendEmail();
        } else {
            Toast.makeText(this, "Пустой список", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendEmail() {
        ArrayList<Uri> fileUris = new ArrayList<Uri>();

        File xlsFile = new File(getExternalFilesDir(null), nameOfFile + ".xls");
        File kmlFile = new File(getExternalFilesDir(null), nameOfFile + ".kml");

        if (!xlsFile.exists()) {
            exportToExcel();
        }

        if (!kmlFile.exists()) {
            exportToKml();
        }

        Uri uriXls = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", xlsFile);
        Uri uriKml = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", kmlFile);

        fileUris.add(uriXls);
        fileUris.add(uriKml);

        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"vladalexeco86@gmail.com"});
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, nameOfFile);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Some text");

        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);
        shareIntent.setType("application/*");

        startActivity(Intent.createChooser(shareIntent, "Share file to..."));

    }

    private void exportToExcel() {
        // экспортирует данные в excel файл

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String err = sharedPreferences.getString("relative_error", "");

        if (TextUtils.isEmpty(err)) {
            err = "20";
        }

        double errToDecimal;

        try {
            errToDecimal = Double.valueOf(err) / 100;
        } catch (Exception ex) {
            errToDecimal = 0.2;
        }

        String plusMinus = "\u00B1";
        HSSFRow hssfRow;
        HSSFCell hssfCell;

        int count = 0; // количество точек значение, которых в 2 и боллее раз больше среднего значения

        ArrayList<Double> searchVals = new ArrayList<>(); // все значения ПОИСКА
        ArrayList<Double> madVals = new ArrayList<>(); // все значения МАД

        Double sumSearch = 0.0;
        Double sumMad = 0.0;

        File filePath = new File(getApplicationContext().getExternalFilesDir(""), nameOfFile + ".xls");

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Points");

        String[] heads = {"Номер", "Северная", "Восточная", "МАД", "Индекс", "Комментарий"};

        HSSFRow firstRow = hssfSheet.createRow(0);
        HSSFCell firstRowsCell;

        HSSFRow secondRow = hssfSheet.createRow(1);

        HSSFRow thirdRow = hssfSheet.createRow(2);

        HSSFRow fourthRow = hssfSheet.createRow(3);

        HSSFRow sixthRow = hssfSheet.createRow(5);

        HSSFRow seventhRow = hssfSheet.createRow(6);

        HSSFRow eighthRow = hssfSheet.createRow(7);

        HSSFRow ninethRow = hssfSheet.createRow(8);

        HSSFRow tenthRow = hssfSheet.createRow(9);

        HSSFRow eleventhRow = hssfSheet.createRow(10);

        for (int k = 0; k < heads.length; k++) {
            firstRowsCell = firstRow.createCell(k);
            firstRowsCell.setCellValue(heads[k]);
        }

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);

            Double currentSearch = Double.valueOf(point.getSearch());
            searchVals.add(currentSearch);

            Double currentMad = Double.valueOf(point.getMad());
            madVals.add(currentMad);

            hssfRow = hssfSheet.createRow(i + 1);

            double absErr = Double.valueOf(point.getMad()) * errToDecimal;

            BigDecimal bd = new BigDecimal(absErr).setScale(2, RoundingMode.HALF_UP);

            String absErrToString = String.valueOf(bd.doubleValue());

            String valMadWithErr = point.getMad() + " " + plusMinus + " " + absErrToString;

            String[] fields = {point.getOrd(), point.getLatitude(), point.getLongitude(), valMadWithErr, point.getIndex(), point.getComment()};

            for (int j = 0; j < fields.length; j++) {
                hssfCell = hssfRow.createCell(j);
                hssfCell.setCellValue(fields[j]);
            }
        }

        for (int i = 0; i < searchVals.size(); i++) {
            sumSearch += searchVals.get(i);
            sumMad += madVals.get(i);
        }

        BigDecimal ads = new BigDecimal(sumSearch / searchVals.size()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal adm = new BigDecimal(sumMad / madVals.size()).setScale(2, RoundingMode.HALF_UP);

        String averageSearch = String.valueOf(ads.doubleValue()); // среднее значение ПОИСКА
        String averageMad = String.valueOf(adm.doubleValue()); // среднее значение МАД

        String minSearchVal = String.valueOf(searchVals.get(searchVals.indexOf(Collections.min(searchVals)))); // минимальное значение ПОИСКА
        String maxSearchVal = String.valueOf(searchVals.get(searchVals.indexOf(Collections.max(searchVals)))); // максимальное значение ПОИСКА
        String minMadVal = String.valueOf(madVals.get(madVals.indexOf(Collections.min(madVals)))); // минимальное значение МАД
        String maxMadVal = String.valueOf(madVals.get(madVals.indexOf(Collections.max(madVals)))); // максимальное значение МАД

        String[] line1 = {"", "ПОИСК", "МАД"};
        String[] line2 = {"МИН", minSearchVal, minMadVal};
        String[] line3 = {"МАКС", maxSearchVal, maxMadVal};
        String[] line4 = {"СРЕД", averageSearch, averageMad};

        String[][] lines = {line1, line2, line3, line4};

        HSSFCell cell;

        for (int i = 0; i < line1.length; i++) {
            cell = firstRow.createCell(i + 8);
            cell.setCellValue(line1[i]);
        }

        for (int i = 0; i < line2.length; i++) {
            cell = secondRow.createCell(i + 8);
            cell.setCellValue(line2[i]);
        }

        for (int i = 0; i < line3.length; i++) {
            cell = thirdRow.createCell(i + 8);
            cell.setCellValue(line3[i]);
        }

        for (int i = 0; i < line4.length; i++) {
            cell = fourthRow.createCell(i + 8);
            cell.setCellValue(line4[i]);
        }

        // Вписываем параметры температуры
        String[] column8 = {"Температура", "Влажность", "Давление", "Скорость ветра", "", "Пройденное расстояние"};
        String[] column9 = {temperatureOnObj, humidityOnObj, pressureOnObj, windOnObj, "", String.valueOf(totalDistance)};
        HSSFRow[] rowsParam = {sixthRow, seventhRow, eighthRow, ninethRow, tenthRow, eleventhRow};

        for (int i = 0; i < column8.length; i++) {
            cell = rowsParam[i].createCell(8);
            cell.setCellValue(column8[i]);
        }

        for (int j = 0; j < column9.length; j++) {
            cell = rowsParam[j].createCell(9);
            cell.setCellValue(column9[j]);
        }

        HSSFCell cell12 = firstRow.createCell(12);
        HSSFCell cell13 = firstRow.createCell(13);

        for (double val: searchVals) {
            if (val >= Double.parseDouble(averageSearch)) count++;
        }

        cell12.setCellValue("Кол-во точек, значение которых в 2 и более раз больше, чем среднее значение:");
        cell13.setCellValue(String.valueOf(count));

        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }

        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToKml() {
        // Экспортирует данные в kml файл
        Point point = points.get(0);
        String lookAtLat = point.getLatitude();
        String lookAtLon = point.getLongitude();

        String lookAtLatDec;
        String lookAtLonDec;

        if (lookAtLat.equals("no data")) {
            lookAtLatDec = "0.0";
        } else {
            lookAtLatDec = gradToDecimal(lookAtLat);
        }

        if (lookAtLon.equals("no data")) {
            lookAtLonDec = "0.0";
        } else {
            lookAtLonDec = gradToDecimal(lookAtLon);
        }

        File filePath = new File(getExternalFilesDir(null), nameOfFile + ".kml");
//        File internalPath = new File(getDir(null, MODE_PRIVATE), "some.xml");
        FileOutputStream fos = null;

        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }

            fos = new FileOutputStream(filePath);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "kml");
            serializer.attribute(null, "xmlns", "http://earth.google.com/kml/2.0");
            serializer.startTag(null, "Document");
            serializer.startTag(null, "name");
            serializer.text("Waypoints");
            serializer.endTag(null, "name");
            serializer.startTag(null, "visibility");
            serializer.text("1");
            serializer.endTag(null, "visibility");
            serializer.startTag(null, "LookAt");
            serializer.startTag(null, "longitude");
            serializer.text(lookAtLonDec);
            serializer.endTag(null, "longitude");
            serializer.startTag(null, "latitude");
            serializer.text(lookAtLatDec);
            serializer.endTag(null, "latitude");
            serializer.startTag(null, "range");
            serializer.text("10000");
            serializer.endTag(null, "range");
            serializer.endTag(null, "LookAt");
            serializer.startTag(null, "Style");
            serializer.attribute(null, "id", "waypoint");
            serializer.startTag(null, "icon");
            serializer.startTag(null, "href");
            serializer.text("root://icons/palette-3.png?x=192&amp;y=96&amp;w=32&amp;h=32");
            serializer.endTag(null, "href");
            serializer.endTag(null, "icon");
            serializer.endTag(null, "Style");
            serializer.startTag(null, "Folder");
            serializer.startTag(null, "name");
            serializer.text("Road points");
            serializer.endTag(null, "name");

            for (Point currentPoint: points) {
                String mLat = currentPoint.getLatitude();
                String mLon = currentPoint.getLongitude();

                String lat;
                String lon;

                if (mLat.equals("no data")) {
                    lat = "0.0";
                } else {
                    lat = gradToDecimal(currentPoint.getLatitude());
                }

                if (mLon.equals("no data")) {
                    lon = "0.0";
                } else {
                    lon = gradToDecimal(currentPoint.getLongitude());
                }

                String date = currentPoint.getDate();
                String time = currentPoint.getTime();

                serializer.startTag(null, "Placemark");
                serializer.startTag(null, "name");
                serializer.text(currentPoint.getOrd());
                serializer.endTag(null, "name");
                serializer.startTag(null, "description");
                serializer.endTag(null, "description");
                serializer.startTag(null, "styleUrl");
                serializer.text("#waypoint");
                serializer.endTag(null, "styleUrl");
                serializer.startTag(null, "Point");
                serializer.startTag(null, "coordinates");
                serializer.text(lon + "," + lat);
                serializer.endTag(null, "coordinates");
                serializer.endTag(null, "Point");
                serializer.startTag(null, "end");
                serializer.startTag(null, "TimeInstant");
                serializer.startTag(null, "timePosition");
                serializer.startTag(null, "time");
                serializer.text(date + "T" + time + "Z");
                serializer.endTag(null, "time");
                serializer.endTag(null, "timePosition");
                serializer.endTag(null, "TimeInstant");
                serializer.endTag(null, "end");
                serializer.endTag(null, "Placemark");
            }

            serializer.endTag(null, "Folder");
            serializer.endTag(null, "Document");
            serializer.endTag(null, "kml");
            serializer.endDocument();
            serializer.flush();
            fos.close();

        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void menuOnClickSettings(MenuItem menuItem) {
        Intent intent = new Intent(JournalActivity.this, PreferenceActivity.class);
        startActivity(intent);
    }
    // Menu methods End

    public void onClickSavePoints() {
        // Сохраняет данные в файл, если есть отмеченные точки. Если нет, то не сохраняет.
        String distance = String.valueOf(totalDistance) + System.getProperty("line.separator");
        String climate = temperatureOnObj + " " + pressureOnObj + " " + humidityOnObj + " " + windOnObj;

        if (points.size() > 0) {
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(nameOfFile, MODE_PRIVATE);
                for (Point point: points) {
                    String ord = point.getOrd();
                    String search = point.getSearch();
                    String mad = point.getMad();
                    String index = point.getIndex();
                    String comment = point.getComment();
                    String latitude = point.getLatitude();
                    String longitude = point.getLongitude();
                    String date = point.getDate();
                    String time = point.getTime();
                    String string = ord + "  " + search + "  " + mad + "  " + index + "  " + comment + "  " + latitude + "  " + longitude  +  "  " + date + "  " + time + System.getProperty("line.separator");
                    fos.write(string.getBytes());
                }

                fos.write(distance.getBytes());
                fos.write(climate.getBytes());

                Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Пустой файл. Не записано.", Toast.LENGTH_SHORT).show();
        }
    }

    private String openFile(String fileName) {
        // Открывает файл и переводит его в строку, которую и возвращает
        FileInputStream fin = null;
        String text = null;

        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String(bytes);
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        if (text != null) {
            if (text.length() > 0) {
                return text;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private ArrayList<Point> stringFileToPointsArrayList(String stringFile) {
        // Переводит строку, stringFile, которая содержит данные из ранее сохраненного файла
        // в список Array<Point>
        ArrayList<Point> points = new ArrayList<>();
        String[] array = stringFile.split("\n");
        for (String str: array) {
            String[] temp = str.split("  ", 9);
            Point point = new Point(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7], temp[8]);
            points.add(point);
        }
        return points;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Срабатывает, после диалогового окна запроса разрешений
        if (requestCode == requestLocationCode) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
            } else {
                Toast.makeText(this, "Not Locate Permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Работа с географическими координатами
    @Override
    public void onLocationChanged(Location location) {
        // Срабатывает после изменения географических координат
        
        double latitude = location.getLatitude(); // текущая широта, выраженная в градусах
        double longitude = location.getLongitude(); // текущая долгота, выраженная в градусах
        formatLatitude = gradToGradMinSec(latitude); // форматированная текущая широта, выраженная в градусах
        formatLongitude = gradToGradMinSec(longitude); // форматированная текущая долгота, выраженная в градусах
        String degreeLine = "Lat: " + formatLatitude + " Lon: " + formatLongitude;
        String lastLine = statusBar.getText().toString();

        if (lastLat != checkCoord && lastLon != checkCoord) {

            // Координаты крайней отмеченной точки, выраженные в градусах и десятых долях
            double lastLatToDecimal = Double.valueOf(gradToDecimal(lastLat));
            double lastLonToDecimal = Double.valueOf(gradToDecimal(lastLon));
            //

            // Текущие координаты, выраженные в радианах
            double latitudeToRad = latitude * pi / 180;
            double longitudeToRad = longitude * pi / 180;
            //

            // Координаты с крайней отмеченной точки, выраженные в радианах
            double lastLatToDecimalRad = lastLatToDecimal * pi / 180;
            double lastLonToDecimalRad = lastLonToDecimal * pi / 180;
            //

            double cosPhi = Math.sin(latitudeToRad) * Math.sin(lastLatToDecimalRad) + Math.cos(latitudeToRad) *
                    Math.cos(lastLatToDecimalRad) * Math.cos(Math.abs(lastLonToDecimalRad - longitudeToRad));

            double phi = Math.acos(cosPhi);

            double distance = radiusOfEarth * phi * 1000; // расстояние до крайней точки, выраженное в метрах

            long distanceRound = Math.round(distance);
            pointDistance = (int) distanceRound;

            lastDistance.setText(String.valueOf(pointDistance) + " м");
        } else {
            lastDistance.setText("0 м");
        }


        if (!lastLine.equals(degreeLine)) {
            Blink blink = new Blink();
            blink.execute();
        }
        statusBar.setText(degreeLine);

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    private String gradToGradMinSec(double grad) {
        // Переводит градусы, выраженные в целых и десятичных долях в формат
        // градус-минута-секунда(два знака после запятых). Возвращает строку.
        int degree = (int) grad;
        double fractPart = grad - degree;
        double minutesFract = fractPart * 60;
        int minutes = (int) minutesFract;
        double sec = (minutesFract - minutes) * 60;

        BigDecimal bd = new BigDecimal(sec).setScale(2, RoundingMode.HALF_UP);

        String secStr = String.valueOf(bd.doubleValue());

        return Integer.toString(degree) + "\u00BA" + Integer.toString(minutes) + "\'" +
                secStr + "\"";
    }

    private String gradToDecimal(String grad) {
        // Переводит градусы, выраженные в целых, минутах и секундах в
        // в формат градуса, как десятичной дроби
        String degreeSymbol = "\u00BA";
        String minuteSymbol = "'";
        String secondSymbol = "\"";

        String[] firstSplit = grad.split(degreeSymbol);
        String degreeVal = firstSplit[0];
        String[] secondSplit = firstSplit[1].split(minuteSymbol);
        String minuteVal = secondSplit[0];
        String secondVal = secondSplit[1].substring(0, secondSplit[1].length() - 1);

        double dval = Double.valueOf(degreeVal);
        double mval = Double.valueOf(minuteVal);
        double sval = Double.valueOf(secondVal);

        double total = dval + (mval / 60) + (sval / 3600);

        BigDecimal bd = new BigDecimal(total).setScale(6, RoundingMode.HALF_UP);

        String res = String.valueOf(bd.doubleValue());

        return res;
    }

    private void deletePointFromRecyclerView(int position) {
        // удаляет точку из таблицы
        points.remove(position);
        pointAdapter.notifyItemRemoved(position);
        for (int i = position; i < points.size(); i++) {
            Point point = points.get(i);
            point.setOrd(String.valueOf(Integer.parseInt(point.getOrd()) - 1));
            points.set(i, point);
            pointAdapter.notifyItemChanged(i);
        }
        pointCount--;

    }
    // -----//-----

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // контекстное меню открывающееся, при долгом нажатии на строку точки МАД в таблице
        if (item.getOrder() == 0) {
            // править данные точки
            Intent intent = new Intent(JournalActivity.this, PointInfoToChange.class);
//            int it = item.getGroupId();
            intent.putExtra(PointInfoToChange.pointPosition, item.getGroupId());
            intent.putExtra(PointInfoToChange.fromJournal, true);
            startActivity(intent);

        } else if (item.getOrder() == 1) {
            // удалить точку
            operation = 1;
            itemId = item.getGroupId();
            QuitDialog dialog = new QuitDialog("Удалить точку " +  String.valueOf(itemId + 1) + "?");
            dialog.show(getSupportFragmentManager(), "quit");
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // срабатывает при нажатии клавиши "back" на телефоне
        operation = 2;
        QuitDialog quitDialog = new QuitDialog("Сохранить и выйти?");
        quitDialog.show(getSupportFragmentManager(), "quit");

    }

    private void saveInCancheLastFileName() {
        // сохраняет имя крайнего открытого файла к кеше
        File cacheFile = new File(getCacheDir(), lastOpenedFile);
        FileOutputStream fos = null;

        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }

            fos = new FileOutputStream(cacheFile);
            fos.write(nameOfFile.getBytes());
        } catch (IOException ex) {
            ex.getStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                ex.getStackTrace();
            }
        }
    }


    private class Blink extends AsyncTask<Void, Void, Void> {
        // класс объекта - "лампочки", которая мигает при смене географических координат
        // операции осуществляются параллельно основному потоку приложения
        @Override
        protected void onPreExecute() {
            round.setBackgroundResource(R.drawable.rounded_textview_red);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SystemClock.sleep(500);
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            round.setBackgroundResource(R.drawable.rounded_textview);
        }

    }


}