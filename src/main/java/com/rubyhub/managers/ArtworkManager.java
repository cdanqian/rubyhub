package com.rubyhub.managers;

import com.rubyhub.models.Artwork;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ArtworkManager extends Manager {
    public static ArtworkManager _self;
    public static String FIELD_ID = "_id",
            FIELD_NAME = "name", FIELD_DESC = "description", FIELD_PRICE = "price", FIELD_SIZE = "sizes", FIELD_STYLES = "styles",
            FIELD_SOLDOUT = "soldout", FIELD_LIKES = "likes", FIELD_PASS_CHECK = "passedcheck", FIELD_STUDENT = "student";

    public static String FIELD_IMAGE_CONTENT = "content";

    public static ArtworkManager getInstance() {
        if (_self == null) {
            return new ArtworkManager();
        }
        return _self;
    }

    public String createArtwork(String name, String desc, Double price, JSONArray size, JSONArray styles, String student) {
        String id = new ObjectId().toString();
        this.artworkCollection.insertOne(
                new Document(FIELD_ID, id).append(FIELD_NAME, name)
                        .append(FIELD_DESC, desc)
                        .append(FIELD_PRICE, price)
                        .append(FIELD_SIZE, convertJSONArrayToList(size))
                        .append(FIELD_STYLES, convertJSONArrayToList(styles))
                        .append(FIELD_SOLDOUT, false)
                        .append(FIELD_LIKES, 0).append(FIELD_PASS_CHECK, false)
                        .append(FIELD_STUDENT, student)
                        .append(FIELD_DELETED, false)
                        .append(FIELD_CREATED_ON, new Date())
                        .append(FIELD_UPDATED_ON, new Date())
                        .append(FIELD_DELETED_ON, null)
        );
        return id;
    }
    public void updateLikes() {

    }

    public Boolean uploadArtworkImage(String id, InputStream image) {

        if (!inspectImageContent()) return false;

        long count = this.artworkImageCollection.countDocuments(eq(FIELD_ID, id));
        if (count == 0) {
            this.artworkImageCollection.insertOne(new Document(FIELD_ID, id)
                    .append(FIELD_IMAGE_CONTENT, image)
                    .append(FIELD_DELETED, false)
                    .append(FIELD_CREATED_ON, new Date())
                    .append(FIELD_UPDATED_ON, new Date())
                    .append(FIELD_DELETED_ON, null)
            );
        } else {
            this.artworkImageCollection.findOneAndUpdate(eq(FIELD_ID, id),
                    new Document("$set", new Document(FIELD_UPDATED_ON, new Date())
                            .append(FIELD_IMAGE_CONTENT, image.toString().getBytes())
                    ));
        }

        this.artworkCollection.findOneAndUpdate(eq(FIELD_ID, id), new Document("$set",new Document(FIELD_PASS_CHECK, true)));

        return true;
    }

    public Boolean inspectImageContent() {
        // todo: image inspection
        return true;
    }

    public Artwork getArtworkById(String id) {
        Document doc = this.artworkCollection.find(eq(FIELD_ID, id)).first();
        Document docImg = this.artworkImageCollection.find(eq(FIELD_ID, id)).first();

        Artwork artwork = new Artwork(doc);
        artwork.setImage(docImg.get(FIELD_IMAGE_CONTENT, Binary.class));
        return artwork;
    }

    public List<Artwork> getArtworks() {
        List<Artwork> artworks = new ArrayList<>();
        Iterable<Document> docs = this.artworkCollection.find(FILTER_NOT_DELETED);
        if (docs != null) {
            docs.forEach(doc -> {
                String id = doc.getString(FIELD_ID);
                Artwork artwork = new Artwork(doc);
                artworks.add(artwork);
            });
            return artworks;
        } else {
            return null;
        }
    }
}
