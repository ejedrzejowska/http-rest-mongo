package com.sda.rest.server.phone;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PhoneDAO {
    private static final MongoClient client =
            new MongoClient("localhost", 27017);
    private static final String PHONES_COLLECTION = "phones";

    public void insertPhones(List<PhoneDTO> phones){
        List<Document> documents = new ArrayList<>();
        for(PhoneDTO phoneDto : phones) {
            Document document = new Document();
            document.append("name",phoneDto.getName());
            document.append("brand",phoneDto.getBrand());
            document.append("touchscreen",phoneDto.getTouchscreen());
            document.append("releaseDate",phoneDto.getReleaseDate());
            document.append("weight",phoneDto.getWeight());
            document.append("ram",phoneDto.getRam());
            document.append("cardStandard",phoneDto.getCardStandard());
            document.append("operatingSystem",phoneDto.getOperatingSystem());
            document.append("displayProtection",phoneDto.getDisplayProtection());

            documents.add(document);
        }
        getCollectionOfPhones().insertMany(documents);
    }

    public List<PhoneDTO> getPhones(){
        FindIterable<Document> documents = getCollectionOfPhones().find();
        List<PhoneDTO> phones = new ArrayList<>();
        for(Document document : documents) {
            PhoneDTO phoneDto = buildPhoneDTO(document);

            phones.add(phoneDto);
        }
        return phones;
    }

    public PhoneDTO getPhoneByName(String phoneName) {
        Document document = getCollectionOfPhones().find(Filters.eq("name", phoneName)).first();
        PhoneDTO phoneDto = buildPhoneDTO(document);

        return phoneDto;
    }

    public PhoneDTO getPhoneById(String id) {
        Document document = getCollectionOfPhones().find(Filters.eq("_id", new ObjectId(id))).first();
        PhoneDTO phoneDto = buildPhoneDTO(document);

        return phoneDto;
    }

    public void updatePhone(PhoneDTO phoneDto){
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.put("name",phoneDto.getName());
        updateFields.put("brand",phoneDto.getBrand());

        // {$set:{name:phoneDto.getName(),brand:phoneDto.getBrand()}}
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.put("$set", updateFields);
        //{_id:{$eq:phoneDto.getId()}}
        BasicDBObject searchQuery = new BasicDBObject("_id",new ObjectId(phoneDto.getId()));
        getCollectionOfPhones().updateOne(searchQuery,setQuery);

    }

    public void deletePhone(String phoneName) {
        BasicDBObject basicDBObject = new BasicDBObject("name",phoneName);
        DeleteResult deleteResult = getCollectionOfPhones().deleteOne(basicDBObject);
        if(deleteResult.getDeletedCount()!=0) {
            System.out.println(phoneName + " phone has been deleted.");
        }
    }

    private MongoCollection<Document> getCollectionOfPhones() {
        return client.getDatabase("phonesDB").getCollection(PHONES_COLLECTION);
    }

    private PhoneDTO buildPhoneDTO(Document document){
        PhoneDTO phoneDto = PhoneDTO.builder()
                .id(document.getObjectId("_id").toString())
                .name(document.getString("name"))
                .brand(document.getString("brand"))
                .ram(document.getDouble("ram"))
                .touchscreen(document.getBoolean("touchscreen"))
                .cardStandard(document.getString("cardStandard"))
                .operatingSystem(document.getString("operatingSystem"))
                .weight(document.getDouble("weight"))
                .displayProtection(document.getString("displayProtection"))
                .build();

        Optional.ofNullable(document.getDate("releaseDate")).ifPresent(elem -> phoneDto
                .setReleaseDate(elem.toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate()));
        return phoneDto;
    }

}
