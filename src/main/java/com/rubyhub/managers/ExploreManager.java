package com.rubyhub.managers;

import com.mongodb.client.model.Filters;
import com.rubyhub.exceptions.AppException;
import com.rubyhub.models.Artwork;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class ExploreManager extends ArtworkManager {
    public static ExploreManager _self;
    private static Bson SORT_BY_DEFAULT = eq(FIELD_LIKES, -1);

    public static ExploreManager getInstance() {
        if (_self == null) {
            return new ExploreManager();
        }
        return _self;
    }

    public boolean markArtworkLikes(String id, Boolean mark) {
        int likes = 0;
        Document doc = this.artworkCollection.find(and(eq(FIELD_ID, id), FILTER_NOT_DELETED)).first();
        Artwork artwork = new Artwork(doc);
        if (mark) {
            likes = artwork.getLikes() + 1;
        } else {
            likes = artwork.getLikes() - 1;
        }
        this.artworkCollection.findOneAndUpdate(eq(FIELD_ID, id),
                new Document("$set", new Document(FIELD_LIKES, likes)))
        ;
        return true;
    }

    public List<Artwork> searchArtworks(String query, String filters, String sortby, Integer offset, Integer count) {

        if (query == null) return this.getArtworks();
        Iterable<Document> docs;

        Document searchFilters = new Document().append("$text", new Document("$search", query));
        if (filters != null) {
            searchFilters = buildFilter(filters, searchFilters);
        }

        docs = this.artworkCollection.find(and(searchFilters, eq(FIELD_PASS_CHECK, true), FILTER_AVAILABLE_ARTWORKS))
                .sort(buildSortBy(sortby))
                .skip(offset).limit(count);

        return castArtworkDoc2List(docs);
    }

    private Document buildFilter(String filters, Document filterOptions) {
        String[] filterList = filters.split("and");
        for (String filter : filterList) {
            String[] options = filter.split("eq");
            if (options[0].trim().toLowerCase().equals("styles")) {
                String values = options[1].trim().substring(1, options[1].length() - 2);
                filterOptions.append(FIELD_STYLES, new Document("$all", Arrays.asList(values.split(","))));
            }
        }

        return filterOptions;
    }

    private Bson buildSortBy(String sortby) {

        switch (sortby) {
            case "timeAsc":
                return eq(FIELD_CREATED_ON, 1);
            case "timeDesc":
                return eq(FIELD_CREATED_ON, -1);
            case "likesAsc":
                return eq(FIELD_LIKES, 1);
            case "likesDesc":
                return eq(FIELD_LIKES, -1);
        }
        return SORT_BY_DEFAULT;
    }

    @Override
    public List<Artwork> getArtworks() {
        Iterable<Document> docs = this.artworkCollection.find(and(eq(FIELD_PASS_CHECK, true), FILTER_AVAILABLE_ARTWORKS))
                                        .sort(SORT_BY_DEFAULT);
        return castArtworkDoc2List(docs);
    }

    public ArrayList<Artwork> getArtworkListSortedByLikes(String sortby) throws AppException {
        try {
            ArrayList<Artwork> artworkList = new ArrayList<>();
            Document sort = null;
            switch (sortby) {
                case "likesAsc":
                    sort = new Document(FIELD_LIKES, 1);
                    break;
                case "likesDesc":
                    sort = new Document(FIELD_LIKES, -1);
                    break;
            }
            Iterable<Document> docs = this.artworkCollection.find(FILTER_NOT_DELETED).sort(sort);
            docs.forEach(doc -> artworkList.add(new Artwork(doc)));
            return new ArrayList<>(artworkList);
        } catch (Exception e) {
            throw handleException("Get Artwork List", e);
        }
    }

    public ArrayList<Artwork> getArtworkListSortedByTime(String sortby) throws AppException {
        try {
            ArrayList<Artwork> artworkList = new ArrayList<>();
            Document sort = null;
            switch (sortby) {
                case "timeAsc":
                    sort = new Document(FIELD_CREATED_ON, 1);
                    break;
                case "timeDesc":
                    sort = new Document(FIELD_CREATED_ON, -1);
                    break;
            }
            Iterable<Document> docs = this.artworkCollection.find(FILTER_NOT_DELETED).sort(sort);
            docs.forEach(doc -> artworkList.add(new Artwork(doc)));
            return new ArrayList<>(artworkList);
        } catch (Exception e) {
            throw handleException("Get Artwork List", e);
        }
    }

    public ArrayList<Artwork> getAllArtworksFilteredByOwners(String student) {
        ArrayList<Artwork> artworks = new ArrayList<>();
        Bson filter = null;
        filter = Filters.eq(FIELD_STUDENT, student);
        Iterable<Document> docs = this.artworkCollection.find(filter);
        if (docs != null) {
            docs.forEach(doc -> artworks.add(new Artwork(doc)));
            return new ArrayList<>(artworks);
        } else {
            return null;
        }
    }
}
