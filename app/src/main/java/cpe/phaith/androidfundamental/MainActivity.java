package cpe.phaith.androidfundamental;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cpe.phaith.androidfundamental.database.UsersDataSource;
import cpe.phaith.androidfundamental.model.User;

public class MainActivity extends ListActivity {
    private UsersDataSource usersDataSource;
    private Context context;
    private Button addUser;
    private Button deleteUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        usersDataSource = new UsersDataSource(context);
        usersDataSource.open();

        addUser = (Button)findViewById(R.id.addUser);
        deleteUser = (Button)findViewById(R.id.deleteUser);

        List<User> values = usersDataSource.getAllUsers();

        ArrayAdapter<User> adapter = new ArrayAdapter<User>(context, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<User> adapter = (ArrayAdapter<User>) getListAdapter();
                User user = null;
                EditText name = (EditText)findViewById(R.id.name);
                String strName = name.getText().toString();
                if(strName.isEmpty()){
                    Toast.makeText(context,"Please enter name to save.", Toast.LENGTH_SHORT).show();
                }else{
                    user = usersDataSource.createUser(strName);
                    adapter.add(user);
                    name.setText("");
                }
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<User> adapter = (ArrayAdapter<User>) getListAdapter();
                User user = null;

                if (getListAdapter().getCount() > 0) {
                    user = (User) getListAdapter().getItem(0);
                    usersDataSource.deleteUser(user);
                    adapter.remove(user);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        usersDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        usersDataSource.close();
        super.onPause();
    }

}

