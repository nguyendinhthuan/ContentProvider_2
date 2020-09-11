package com.example.contentprovider_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText edt_ID, edt_Name, edt_Unit, edt_Madein;
    private Button btn_Exit, btn_Save, btn_Select, btn_Delete, btn_Update;
    private GridView gridView;

    static final String AUTHORITY = "com.example.myapplication";
    static final String CONTENT_PROVIDER = "contentprovider";
    static final String URL = "content://" + AUTHORITY + "/" + CONTENT_PROVIDER;
    static final Uri CONTENT_URI = Uri.parse(URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        Save();
        Select();
        Delete();
        Update();
    }

    public void AnhXa() {
        edt_ID = (EditText) findViewById(R.id.edt_ID);
        edt_Name = (EditText) findViewById(R.id.edt_Name);
        edt_Unit = (EditText) findViewById(R.id.edt_Unit);
        edt_Madein = (EditText) findViewById(R.id.edt_Madein);

        btn_Exit = (Button) findViewById(R.id.btn_Exit);
        btn_Save = (Button) findViewById(R.id.btn_Save);
        btn_Select = (Button) findViewById(R.id.btn_Select);
        btn_Delete = (Button) findViewById(R.id.btn_Delete);
        btn_Update = (Button) findViewById(R.id.bt_Update);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = (Math.round(i/4) * 4);
                edt_ID.setText(adapterView.getItemAtPosition(id).toString());
                edt_Name.setText(adapterView.getItemAtPosition(id+1).toString());
                edt_Unit.setText(adapterView.getItemAtPosition(id+2).toString());
                edt_Madein.setText(adapterView.getItemAtPosition(id+3).toString());
            }
        });
    }

    public void Save() {
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", edt_ID.getText().toString());
                contentValues.put("name", edt_Name.getText().toString());
                contentValues.put("unit", edt_Unit.getText().toString());
                contentValues.put("madein", edt_Madein.getText().toString());

                Uri insert_uri = getContentResolver().insert(CONTENT_URI, contentValues);
                Toast.makeText(MainActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Select() {
        btn_Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list_string = new ArrayList<>();
                Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, "name");
                if (cursor != null) {
                    cursor.moveToFirst();
                    do {
                        list_string.add(cursor.getInt(0) + "");
                        list_string.add(cursor.getString(1));
                        list_string.add(cursor.getString(2));
                        list_string.add(cursor.getString(3));
                    } while (cursor.moveToNext());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list_string);
                    gridView.setAdapter(adapter);
                }
            }
        });
    }

    public void Delete() {
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    final int id = Integer.parseInt(edt_ID.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Xóa");
                    builder.setMessage("Bạn có chắc muốn xóa author id " + id );
                    builder.setCancelable(true);
                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int count = getContentResolver().delete(CONTENT_URI, "id = ? ", new String[]{ id +""});
                            if(count > 0) {
                                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(MainActivity.this, "Xóa không thành công", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void Update() {
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", edt_ID.getText().toString());
                    contentValues.put("name", edt_Name.getText().toString());
                    contentValues.put("unit" , edt_Madein.getText().toString());
                    contentValues.put("madein" , edt_Unit.getText().toString());

                    int count = getContentResolver().update(CONTENT_URI, contentValues, "id = ? " , new String[]{edt_ID.getText().toString()});
                    if(count > 0) {
                        Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
