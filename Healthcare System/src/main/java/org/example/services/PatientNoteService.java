package org.example.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.config.MongoDBConfig;
import org.example.models.PatientNote;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PatientNoteService {
    private static PatientNoteService instance;
    private MongoCollection<Document> collection;

    private PatientNoteService() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        this.collection = database.getCollection("patient_notes");
    }

    public static PatientNoteService getInstance() {
        if (instance == null) {
            instance = new PatientNoteService();
        }
        return instance;
    }

    public void addNote(PatientNote note) {
        Document doc = new Document("patientID", note.getPatientID())
                .append("doctorID", note.getDoctorID())
                .append("note", note.getNote())
                .append("type", note.getType())
                .append("timestamp", note.getTimestamp().toString());
        collection.insertOne(doc);
    }

    public List<PatientNote> getNotesByPatient(Integer patientID) {
        List<PatientNote> notes = new ArrayList<>();
        collection.find(eq("patientID", patientID)).forEach(doc -> {
            PatientNote note = new PatientNote();
            note.setId(doc.getObjectId("_id").toString());
            note.setPatientID(doc.getInteger("patientID"));
            note.setDoctorID(doc.getInteger("doctorID"));
            note.setNote(doc.getString("note"));
            note.setType(doc.getString("type"));
            note.setTimestamp(LocalDateTime.parse(doc.getString("timestamp")));
            notes.add(note);
        });
        return notes;
    }
}
