package zeev.fraiman.geodatabylocalityname;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.location.Geocoder;
import android.location.Address;


public class GeoActivity extends AppCompatActivity {

    private EditText etCityName, etCountryName;
    private Button btnFindCoordinates;
    private TextView tvResult;
    private NetworkReceiver networkReceiver;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);

        etCityName = findViewById(R.id.etCityName);
        etCountryName = findViewById(R.id.etCountryName);
        btnFindCoordinates = findViewById(R.id.btnFindCoordinates);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);

        btnFindCoordinates.setOnClickListener(v -> {
            String cityName = etCityName.getText().toString().trim();
            String countryName = etCountryName.getText().toString().trim();

            if (cityName.isEmpty() || countryName.isEmpty()) {
                tvResult.setText("Введите название города и страны.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            findCoordinates(cityName, countryName);
        });

        networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }

    private void findCoordinates(String cityName, String countryName) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(cityName + ", " + countryName, 1);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);

                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        double latitude = address.getLatitude();
                        double longitude = address.getLongitude();
                        tvResult.setText(String.format("Latitude:   %.6f\nLongitude: %.5f", latitude, longitude));
                    } else {
                        tvResult.setText("Not found information");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvResult.setText("Error while receiving data");
                });
            }
        }).start();
    }

    private class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            btnFindCoordinates.setEnabled(isConnected);
            if (!isConnected) {
                tvResult.setText("There is no internet connection");
            }
        }
    }
}
