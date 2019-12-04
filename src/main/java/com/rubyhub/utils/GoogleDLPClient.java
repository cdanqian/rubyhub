package com.rubyhub.utils;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dlp.v2.DlpServiceClient;
import com.google.cloud.dlp.v2.DlpServiceSettings;
import com.google.privacy.dlp.v2.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoogleDLPClient {
    public static DlpServiceClient client;
    private static GoogleDLPClient _self;
    private static ByteContentItem.Builder byteItemBuilder = ByteContentItem.newBuilder();
    private static String PROJECT_ID = "rubyhub";

    private GoogleDLPClient() {
        File configFile = new File("/Users/qiandan/IdeaProjects/rubyhub/src/main/java/com/rubyhub/config/Rubyhub-f691186615de.json");

        CredentialsProvider credentialsProvider = null;
        try {
            credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream(configFile)));
            DlpServiceSettings settings = DlpServiceSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
            client = DlpServiceClient.create(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GoogleDLPClient getInstance() {
        if (_self == null) {
            return new GoogleDLPClient();
        }
        return _self;
    }


    public GoogleDLPClient setImageType(String fType) {
        if (fType.toLowerCase().equals("png")) {
            byteItemBuilder.setType(ByteContentItem.BytesType.IMAGE_PNG);
        } else {
            byteItemBuilder.setType(ByteContentItem.BytesType.IMAGE_JPEG);
        }
        return this;
    }

    public Map<Boolean, ArrayList<String>> inspectImage(byte[] fileContent) {
        int maxFindings = 0;
        boolean includeQuote = true;
        ArrayList<String> messages = new ArrayList<String>();
        HashMap<Boolean, ArrayList<String>> inspectionResult = new HashMap<>();

        InspectConfig.FindingLimits findingLimits =
                InspectConfig.FindingLimits.newBuilder().setMaxFindingsPerItem(maxFindings).build();
        InspectConfig inspectConfig =
                InspectConfig.newBuilder()
                        .setLimits(findingLimits)
                        .setIncludeQuote(includeQuote)
                        .build();

        ByteContentItem byteContentItem = byteItemBuilder.setData(ByteString.copyFrom(fileContent))
                .build();

        ContentItem contentItem = ContentItem.newBuilder().setByteItem(byteContentItem).build();

        InspectContentRequest request =
                InspectContentRequest.newBuilder()
                        .setParent(ProjectName.of(PROJECT_ID).toString())
                        .setInspectConfig(inspectConfig)
                        .setItem(contentItem)
                        .build();

        InspectContentResponse response = client.inspectContent(request);

        InspectResult result = response.getResult();
        if (result.getFindingsCount() > 0) {
            messages.add("Image include following sensitive findings, please change and upload again:");
            for (Finding finding : result.getFindingsList()) {
                if (includeQuote) {
                    messages.add("Quote: " + finding.getQuote());
                }
                messages.add("    Info type: " + finding.getInfoType().getName());
                messages.add("    Likelihood: " + finding.getLikelihood());
                inspectionResult.put(false, messages);
            }
        } else {
            messages.add("Passed check!");
            inspectionResult.put(true, messages);
        }
        return inspectionResult;
    }
}