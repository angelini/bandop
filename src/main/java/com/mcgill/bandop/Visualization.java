package com.mcgill.bandop;

import com.mcgill.bandop.models.Design;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;

public class Visualization {

    private final static String URL = "http://dp-kpatel.rhcloud.com/send";

    public static void sendReward(Database db, int designId, double reward) {
        String[] MABInfo = Design.loadMABInfo(db, designId);

        if (MABInfo[0] == null) {
            System.out.println("No MAB ID for design: " + designId);
            return;
        }

        VisualizationData data = new VisualizationData(MABInfo[1], new DateTime(DateTimeZone.UTC), reward);
        VisualizationObject object = new VisualizationObject(MABInfo[0], data);

        Thread req = new Thread(new VisualizationRequest(object));
        req.start();
    }

    static class VisualizationRequest implements Runnable {

        private VisualizationObject object;

        public VisualizationRequest(VisualizationObject object) {
            this.object = object;
        }

        @Override
        public void run() {
            ObjectMapper mapper = new ObjectMapper(  );
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost req = new HttpPost(URL);

            try {
                System.out.println("JSON: " + mapper.writeValueAsString(object));
                StringEntity body = new StringEntity(mapper.writeValueAsString(object), ContentType.APPLICATION_JSON);
                req.setEntity(body);

                CloseableHttpResponse res = client.execute(req);
                res.close();

            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            }
        }

    }

    static class VisualizationObject {

        private String id;
        private VisualizationData data;

        public VisualizationObject() {

        }

        public VisualizationObject(String id, VisualizationData data) {
            this.id = id;
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public VisualizationData getData() {
            return data;
        }

        public void setData(VisualizationData data) {
            this.data = data;
        }

    }

    static class VisualizationData {

        private String alternative;
        private DateTime time;
        private double reward;

        public VisualizationData() {

        }

        public VisualizationData(String alternative, DateTime time, double reward) {
            this.alternative = alternative;
            this.time = time;
            this.reward = reward;
        }

        public String getAlternative() {
            return alternative;
        }

        public void setAlternative(String alternative) {
            this.alternative = alternative;
        }

        public DateTime getTime() {
            return time;
        }

        public void setTime(DateTime time) {
            this.time = time;
        }

        public double getReward() {
            return reward;
        }

        public void setReward(double reward) {
            this.reward = reward;
        }

    }


}
