package org.example.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.config.MongoDBConfig;
import org.example.models.MedicalLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MedicalLogService {
    private static MedicalLogService instance;
    private MongoCollection<Document> collection;

    private MedicalLogService() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        this.collection = database.getCollection("medical_logs");
    }

    public static MedicalLogService getInstance() {
        if (instance == null) {
            instance = new MedicalLogService();
        }
        return instance;
    }

    public void addLog(MedicalLog log) {
        Document doc = new Document("patientID", log.getPatientID())
                .append("action", log.getAction())
                .append("details", log.getDetails())
                .append("performedBy", log.getPerformedBy())
                .append("timestamp", log.getTimestamp().toString());
        collection.insertOne(doc);
    }

    public List<MedicalLog> getLogsByPatient(Integer patientID) {
        List<MedicalLog> logs = new ArrayList<>();
        collection.find(eq("patientID", patientID)).forEach(doc -> {
            MedicalLog log = new MedicalLog();
            log.setId(doc.getObjectId("_id").toString());
            log.setPatientID(doc.getInteger("patientID"));
            log.setAction(doc.getString("action"));
            log.setDetails(doc.getString("details"));
            log.setPerformedBy(doc.getString("performedBy"));
            log.setTimestamp(LocalDateTime.parse(doc.getString("timestamp")));
            logs.add(log);
        });
        return logs;
    }
}
