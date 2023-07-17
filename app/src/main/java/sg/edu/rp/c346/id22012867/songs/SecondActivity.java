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
    ArrayList<Song> filteredSongs;

    Spinner spnSongs;
    Button btn5Stars;
    CustomAdapter adapter, aaNewSongs;

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

// Step 1: Create ArrayAdapter for the Spinner
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getUniqueYears(songs));
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSongs.setAdapter(yearAdapter);

        // Step 2: Set OnItemSelectedListener for the Spinner
        spnSongs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedYear = (Integer) parent.getItemAtPosition(position);
                if (selectedYear == 0) {
                    adapter.clear();
                    adapter.addAll(songs);
                } else {
                    filterSongsByYear(selectedYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
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
    // Helper method to get unique years from the list of songs
    private ArrayList<Integer> getUniqueYears(ArrayList<Song> songs) {
        ArrayList<Integer> years = new ArrayList<>();
        for (Song song : songs) {
            if (!years.contains(song.getYear())) {
                years.add(song.getYear());
            }
        }
        return years;
    }

    // Helper method to filter songs by the selected year
    private void filterSongsByYear(int selectedYear) {
        filteredSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getYear() == selectedYear) {
                filteredSongs.add(song);
            }
        }
        adapter.clear(); // Clear the existing adapter data
        adapter.addAll(filteredSongs); // Add the filtered songs to the adapter
        adapter.notifyDataSetChanged(); // Notify the ListView to update the display
    }
}

