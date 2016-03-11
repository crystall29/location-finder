package com.example.locationfinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class LocListActivity extends Activity implements OnItemClickListener {
    ListView listView;
    String[] category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_list);

        listView = (ListView) findViewById(R.id.listView1);
        httpmethod();
        listView.setOnItemClickListener(this);
    }

    private void httpmethod() {

        Toast.makeText(this, "latitude" + MainActivity.latitude + "\n longitude"
                        + MainActivity.longitude + "\n type" + MainActivity.type,
                Toast.LENGTH_SHORT).show();
        final String QUERY = "http://muskanjain.esy.es/atm.php?mylat=" +
                MainActivity.latitude + "&mylong=" +
                MainActivity.longitude + "&type=" +
                MainActivity.type;
        String url = QUERY.replaceAll("\\s", "");
        HttpClient client = new DefaultHttpClient();
        InputStream inputStream = null;
        try {
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse;
            HttpContext localContext = new BasicHttpContext();
            httpResponse = client.execute(request, localContext);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            String message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null && responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = entity.getContent();
                String str = convertStreamToString(inputStream);

                JSONArray response = new JSONArray(str);
                category = new String[response.length()];

                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = (JSONObject) response.get(i);
                    String name = (String) jsonObject.get("Name");
                    String address = (String) jsonObject.get("Address");
                    category[i] = name + " ," + address;
                }

                if (response.length() == 0) {
                    Toast.makeText(getBaseContext(), "No Updates", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, category);
                    listView.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Data Not Available Due to Network Problem. Please Try Again After Some Time.",
                    Toast.LENGTH_SHORT).show();
        } finally {
            client.getConnectionManager().shutdown();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String data = (String) adapterView.getItemAtPosition(position);
        String guri = "geo:0,0?q=" + data;
        Uri sguri = Uri.parse(guri);
        Intent i = new Intent(Intent.ACTION_VIEW, sguri);
        startActivity(i);
    }

    private String convertStreamToString(InputStream inputStream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_loc_list,
                    container, false);
            return rootView;
        }
    }
}
