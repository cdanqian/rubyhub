package com.rubyhub.managers;

import com.rubyhub.utils.AppLogger;
import com.rubyhub.utils.GoogleDLPClient;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageInspectionManager extends Manager {
    public static ImageInspectionManager _self;
    private boolean passed;
    private List<String> messages;

    public static ImageInspectionManager getInstance() {
        if (_self == null) {
            return new ImageInspectionManager();
        }
        return _self;
    }

    public ImageInspectionManager doInspection(InputStream image, String type) {
        try {
            Map<Boolean, ArrayList<String>> result = GoogleDLPClient.getInstance().setImageType(type).inspectImage(IOUtils.toByteArray(image));
            for (Map.Entry<Boolean, ArrayList<String>> entry : result.entrySet()) {
                this.passed = entry.getKey();
                this.messages = entry.getValue();
            }
        } catch (Exception e) {
            AppLogger.error("Inspection Manager", e);
        }
        return this;
    }

    public boolean getPassed() {
        return passed;
    }

    public List<String> getMessages() {
        return messages;
    }

}
