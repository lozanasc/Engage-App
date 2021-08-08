package com.fvoz.engageapp.Menu.Content;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.Menu.PageViews.ProjectsViewPage;
import com.fvoz.engageapp.R;
import com.fvoz.engageapp.Utilities.ProjectTemplate;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChurchProjectListPage extends AppCompatActivity {

    List<ProjectTemplate> ProjectList;
    ListView ProjectListView;
    ProjectListAdapter adapterProjectList;

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_project_page);

        ProjectListView = (ListView) findViewById(R.id.ChurchProjectsList);
        ProjectList = new ArrayList<>();
        GetProjectList();

        ImageButton BackButton = findViewById(R.id.ProjectBackButton);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackToDashboard = new Intent(ChurchProjectListPage.this, Dashboard.class);
                startActivity(BackToDashboard);
            }
        });
    }

    private boolean BooleanConverter(int bool_arg){
        return bool_arg == 1;
    }

    private void GetProjectList(){
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        String requestURL = sharedPrefs.getString("prefUrl", "default")+"/engage/project/v1/projects/list";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject stringRequestResult = new JSONObject(response);
                    JSONArray list = (JSONArray) stringRequestResult.getJSONArray("list");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject dataObject = list.getJSONObject(i);
                        ProjectTemplate ProjectObject = new ProjectTemplate();

                            String Title = dataObject.getString("name");
                            String Description = dataObject.getString("description");
                            boolean Status = BooleanConverter(dataObject.getInt("status"));
                            String Image = dataObject.getString("image");

                            ProjectObject.setProjectTitle(Title);
                            ProjectObject.setProjectDescription(Description);
                            ProjectObject.setProjectStatus(Status);
                            ProjectObject.setProjectImage(Image);

                        ProjectList.add(ProjectObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterProjectList = new ProjectListAdapter();
                ProjectListView.setAdapter(adapterProjectList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChurchProjectListPage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                sharedPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
                String secret_token = sharedPrefs.getString("token", "default");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + secret_token);
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    public class ProjectListAdapter extends BaseAdapter {

        SharedPreferences sharedPrefs;

        @Override
        public int getCount() {
            return ProjectList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);

            convertView = getLayoutInflater().inflate(R.layout.project_card, parent, false);

            TextView ProjectTitle = convertView.findViewById(R.id.ProjectTitle);
            TextView ProjectDatePosted = convertView.findViewById(R.id.ProjectDate);
            TextView ProjectUpdateDesc = convertView.findViewById(R.id.ProjectUpdateDesc);
            ImageView ProjectThumbnail = convertView.findViewById(R.id.ProjectPhoto);

            ProjectTitle.setText(ProjectList.get(position).getProjectTitle());
            ProjectDatePosted.setText(ProjectList.get(position).isProjectStatus() ? "ACTIVE" : "DELETED");
            ProjectUpdateDesc.setText(ProjectList.get(position).getProjectDescription());
            Picasso.get().load(sharedPrefs.getString("prefUrl", "default")+"/uploads/"+ProjectList.get(position).getProjectImage()).into(ProjectThumbnail);

            String ImageToPageView = ProjectList.get(position).getProjectImage();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ToProjectViewPage = new Intent(ChurchProjectListPage.this, ProjectsViewPage.class);
                    ToProjectViewPage.putExtra("ProjectTitle", ProjectTitle.getText());
                    ToProjectViewPage.putExtra("ProjectDatePosted", ProjectDatePosted.getText());
                    ToProjectViewPage.putExtra("ProjectUpdateDesc", ProjectUpdateDesc.getText());
                    ToProjectViewPage.putExtra("ProjectImage", ImageToPageView);

                    startActivity(ToProjectViewPage);
                }
            });

            return convertView;
        }
    }

}