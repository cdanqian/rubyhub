package com.rubyhub.managers;

import com.rubyhub.models.Artwork;
import com.rubyhub.utils.AppLogger;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class ArtworkManager extends Manager {
    public static ArtworkManager _self;
    public static String FIELD_ID = "_id",
            FIELD_NAME = "name", FIELD_DESC = "description", FIELD_PRICE = "price", FIELD_SIZE = "sizes", FIELD_STYLES = "styles",
            FIELD_SOLDOUT = "soldout", FIELD_LIKES = "likes", FIELD_PASS_CHECK = "passedcheck", FIELD_STUDENT = "student", FIELD_FTYPE = "type";

    public static String FIELD_IMAGE_CONTENT = "content",FIELD_IMAGE_TYPE = "type";

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

    public Boolean updateArtwork(String id, String name, String desc, JSONArray styles) {
        long count = this.artworkCollection.countDocuments(eq(FIELD_ID, id));
        if (count == 0) return false;
        this.artworkCollection.findOneAndUpdate(eq(FIELD_ID, id),
                new Document("$set", new Document(FIELD_NAME, name)
                        .append(FIELD_DESC, desc)
                        .append(FIELD_STYLES, convertJSONArrayToList(styles)).append(FIELD_UPDATED_ON, new Date())))
        ;
        return true;
    }

    public Boolean uploadArtworkImage(String id, InputStream image, String type) {
        String source = "";
        if (!inspectImageContent()) return false;
        try {
            byte[] encoded = IOUtils.toByteArray(image);

            long count = this.artworkImageCollection.countDocuments(eq(FIELD_ID, id));
            if (count == 0) {
                this.artworkImageCollection.insertOne(new Document(FIELD_ID, id)
                        .append(FIELD_IMAGE_CONTENT, encoded)
                        .append(FIELD_FTYPE, type)
                        .append(FIELD_CREATED_ON, new Date())
                        .append(FIELD_UPDATED_ON, new Date())
                );
            } else {
                this.artworkImageCollection.findOneAndUpdate(eq(FIELD_ID, id),
                        new Document("$set", new Document(FIELD_UPDATED_ON, new Date())
                                .append(FIELD_IMAGE_CONTENT, encoded)
                                .append(FIELD_FTYPE, type)
                        ));
            }
            this.artworkCollection.findOneAndUpdate(eq(FIELD_ID, id), new Document("$set", new Document(FIELD_PASS_CHECK, true)));

            source = id + "." + type;
            return true;
        } catch (IOException e) {
            AppLogger.error("Image convert error", e);
            return false;
        }

    }

    public Map getArtworkImageById(String id) {
        Map<String, Object> image = new HashMap<>();
        Document doc = this.artworkImageCollection.find(eq(FIELD_ID, id)).first();
        image.put("type",doc.getString(FIELD_IMAGE_TYPE));
        image.put("content",doc.get(FIELD_IMAGE_CONTENT, Binary.class).getData());
        return image;
    }

    public Boolean inspectImageContent() {
        // todo: image inspection
        return true;
    }


    public Artwork getArtworkById(String id) {
        Document doc = this.artworkCollection.find(and(eq(FIELD_ID, id),FILTER_NOT_DELETED)).first();
        if(doc == null) return null;

        Artwork artwork = new Artwork(doc);
        if (this.artworkImageCollection.countDocuments(eq(FIELD_ID, id)) != 0) {
            Document image = this.artworkImageCollection.find(eq(FIELD_ID, id)).first();
            artwork.setImage(id, image.getString("type"));
        }
        return artwork;
    }

    public void deleteArtwork(String id) {
        this.artworkCollection.findOneAndUpdate(eq(FIELD_ID, id),
                new Document("$set", new Document(FIELD_DELETED, true)
                        .append(FIELD_DELETED_ON, new Date())
                        .append(FIELD_PASS_CHECK, false)));
        this.artworkImageCollection.findOneAndDelete(eq(FIELD_ID, id));

    }

    public List<Artwork> getArtworks() {
        List<Artwork> artworks = new ArrayList<>();
        Iterable<Document> docs = this.artworkCollection.find(FILTER_NOT_DELETED);
        if (docs != null) {
            docs.forEach(doc -> {
                String id = doc.getString(FIELD_ID);
                Artwork artwork = new Artwork(doc);
                if (this.artworkImageCollection.countDocuments(eq(FIELD_ID, id)) != 0) {
                    Document image = this.artworkImageCollection.find(eq(FIELD_ID, id)).first();
                    artwork.setImage(id, image.getString("type"));
                }
                artworks.add(artwork);
            });
            return artworks;
        } else {
            return null;
        }
    }

    public void resetData() {
        this.artworkCollection.drop();
        this.artworkImageCollection.drop();
        JSONArray sizes = new JSONArray().put("S").put("M").put("L");
        JSONArray styles = new JSONArray().put("modern").put("abstract");
        createArtwork("artwork 1", "this is artwork 1", 123.3, sizes, styles, "owner 1");
        createArtwork("artwork 2", "this is artwork 2", 223.3, sizes, styles, "owner 2");
        createArtwork("artwork 3", "this is artwork 3", 323.3, sizes, styles, "owner 3");
    }
}
