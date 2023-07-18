package sg.edu.rp.c346.id22012867.songs;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


public class SecondActivity extends AppCompatActivity {

    ListView lvSongs;
    //ArrayAdapter<Song> aaSongs;
    //ArrayAdapter<Song> aaNewSongs;
    ArrayList<Song> songs;
    ArrayList<Song> newSongs;
    ArrayList<Song> filteredSong;

    Spinner spnSongs;
    Button btn5Stars;
    CustomAdapter adapter, aaNewSongs, aaFiltered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        lvSongs = findViewById(R.id.lvSongs);
        spnSongs = findViewById(R.id.spinnerYear);
        btn5Stars = findViewById(R.id.btn5Stars);

        DBHelper dbHelper = new DBHelper(this);
        songs = dbHelper.getSongs();
        dbHelper.close();


        //aaSongs = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songs);
        adapter = new CustomAdapter(this, R.layout.row, songs);
        //lvSongs.setAdapter(aaSongs);
        lvSongs.setAdapter(adapter);

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        spnSongs.setAdapter(adapter);

        spnSongs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();

                filteredSong = new ArrayList<>();
                for (int i = 0; i < songs.size(); i++) {
                    if (String.valueOf(songs.get(i).getYear()).equals(selectedYear)) {
                        filteredSong.add(songs.get(i));
                    }
                }
                aaFiltered = new CustomAdapter(SecondActivity.this, R.layout.row, filteredSong);
                lvSongs.setAdapter(aaFiltered);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn5Stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSongs = new ArrayList<>();
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).getStars() == 5) {
                        newSongs.add(songs.get(i));
                    }
                }
                aaNewSongs = new CustomAdapter(SecondActivity.this, R.layout.row, newSongs);
                lvSongs.setAdapter(aaNewSongs);
            }
        });

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song data = songs.get(position);
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

    }

}

