package com.example.externalstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.myapp.getAppContext;


public class MainActivity extends ListActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    ArrayList<String> myList;
    ArrayAdapter arrayAdapter;
    String dirPath = "";
    String rootDir = "";
    public String parentDirPath = "";
    ArrayList<String> theNamesOfFiles;


    TextView textView;
    TextView parentPath;
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForExternalStoragePermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestForExternalStoragePermission();

//        dirPath = "/storage/C8F3-17E9";
//        rootDir = "/storage/C8F3-17E9";

        dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        textView = findViewById(R.id.text_view);
        parentPath = findViewById(R.id.parent_path);
        String state = Environment.getExternalStorageState();


        textView.setText(dirPath);
        parentPath.setText("Quay lại");


        show();
        this.registerForContextMenu(this.getListView());
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    parentDirPath = dirPath + "/..";
                    // dirPath = dirPath+"/"+theNamesOfFiles.get(position);
                    File f = new File(dirPath + "/" + theNamesOfFiles.get(position));
                    if (f.isDirectory()) {
                        dirPath = dirPath + "/" + theNamesOfFiles.get(position);
                        show();
                    } else if (f.isFile()) {
                        textView.setText(f.getName());
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        File file = new File(dirPath + "/" + theNamesOfFiles.get(position));

                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                        String type = mime.getMimeTypeFromExtension(ext);
                        textView.setText(type);
                        intent.setDataAndType(FileProvider.getUriForFile( getAppContext(), getApplicationContext().getPackageName() + ".provider",file), type);
                        startActivity(intent);
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.setAction(android.content.Intent.ACTION_VIEW);
//                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        File file = new File(dirPath + "/" + theNamesOfFiles.get(position));
//
//                        MimeTypeMap mime = MimeTypeMap.getSingleton();
//                        String ext = file.getName().substring(file.getName().indexOf(".") + 1);
//                        String type = mime.getMimeTypeFromExtension(ext);
//                        intent.setDataAndType(Uri.fromFile(file), type);
//                        startActivity(intent);



                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onParentDir_Click(View view) {
        if (dirPath != "" && dirPath != "/" && dirPath.equals(rootDir) == false) {
            String[] folders = dirPath.split("\\/");
            String[] folders2 = {};
            folders2 = Arrays.copyOf(folders, folders.length - 1);
            dirPath = TextUtils.join("/", folders2);
        }

        if (dirPath == "") {
            dirPath = "/";
        }

        show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.btn_delete:

                textView.setText(myList.get(info.position));


                AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
                dialogDelete.setTitle("Xóa");
                dialogDelete.setMessage("Nhấn Có để xóa:");
                dialogDelete.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File f = new File(myList.get(info.position));
                        Toast.makeText(getApplicationContext(), "Exists" + f.exists(), 1).show();

                        boolean success;
                        if (f.isDirectory()) {
                            String[] child = f.list();
                            for (int i = 0; i < child.length; i++) {
                                new File(f, child[i]).delete();
                            }
                        }

                        success = f.delete();

                        if (!success) {
                            Toast.makeText(getApplicationContext(), "Delete op. failed.", 1).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "File deleted.", 1).show();
                        }

                        show();
                        }

                    });
                dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogDelete.create();
                b.show();

                break;
            case R.id.btn_rename:
                textView.setText("rename");
                AlertDialog.Builder dialogRename = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.newfolder, null);
                dialogRename.setView(dialogView);

                final EditText txtNewFolder = (EditText) dialogView.findViewById(R.id.newfolder);

                dialogRename.setTitle("Rename Folder");
                dialogRename.setMessage("Enter name of new folder");
                dialogRename.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        File f = new File(myList.get(info.position));
                        File fRename = new File(dirPath+"/"+txtNewFolder.getText().toString());
                        f.renameTo(fRename);
                        //Refresh ListView
                        show();
                    }
                });
                dialogRename.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog c = dialogRename.create();
                c.show();
                break;
        }
        return super.onContextItemSelected(item);
    }


    private void show() {
        myList = new ArrayList<>();
        theNamesOfFiles = new ArrayList<>();
        File dir = new File(dirPath);
        if (dir.exists()) {
            Log.d("path", dir.toString());
            File list[] = dir.listFiles();
            for (int i = 0; i < list.length; i++) {
                myList.add(list[i].getAbsolutePath());
                theNamesOfFiles.add(list[i].getName());
            }
            arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, theNamesOfFiles);
            setListAdapter(arrayAdapter);
        }
        textView.setText(dirPath);
    }
}

