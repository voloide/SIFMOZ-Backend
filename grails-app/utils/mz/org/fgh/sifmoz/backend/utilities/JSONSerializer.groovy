package mz.org.fgh.sifmoz.backend.utilities

import grails.web.*
import mz.org.fgh.sifmoz.backend.base.BaseEntity
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import java.nio.charset.StandardCharsets

class JSONSerializer {
    def target
    boolean searchMode
    List<String> toInclude

    JSONSerializer(def target){
        this.target = target
    }

    JSONSerializer(def target, boolean searchMode, List<String> toInclude){
        this.target = target
        this.searchMode = searchMode
        this.toInclude = toInclude
    }

    String getJSON() {
        Closure jsonFormat = {
                // Set the delegate of buildJSON to ensure that missing methods called thereby are routed to the JSONBuilder

            if (this.searchMode) {
                buildLightJSON.delegate = delegate
                buildLightJSON(target)
            } else {
                buildJSON.delegate = delegate
                buildJSON(target)
            }
        }
        def json = new JSONBuilder().build(jsonFormat)
        return json.toString(true)
    }

    private buildJSON = { obj ->
        setProperty("id", obj.id )
        obj.properties.each { propName, propValue ->
            if (!['class', 'metaClass'].contains(propName)) {

                if (isSimple(propValue)) {
                    // It seems "propName = propValue" doesn't work when propName is dynamic so we need to
                    // set the property on the builder using this syntax instead
                    setProperty(propName, propValue)
                } else {
                    // create a nested JSON object and recursively call this function to serialize it
                    Closure nestedObject = {
                        buildJSON(propValue)
                    }
                    setProperty(propName, nestedObject)
                }
            }
        }
    }

    private buildLightJSON = { obj ->
        setProperty("id", obj.id )
        obj.properties.each { propName, propValue ->
            if (!['class', 'metaClass'].contains(propName)) {

                if (isSimple(propValue)) {
                    setProperty(propName, propValue)
                } else if (isSimpleIncluded(propName)) {
                    setProperty(propName, propValue)
                }
            }
        }
    }

    private buildLightJSONTest = { obj ->
        setProperty("id", obj.id )
        obj.properties.each { propName, propValue ->
            if (!['class', 'metaClass'].contains(propName)) {

                if (isSimple(propValue)) {
                    setProperty(propName, propValue)
                } else if (isSimpleIncluded(propName)) {
                    setProperty(propName, propValue)
                } else if (iscompositionIncluded(propName)) {
                    System.out.println(propName)
                    Closure nestedObject = {
                        buildLightJSON(propValue)
                    }
                    setProperty(propName, nestedObject)

                }
            }
        }
    }

    private boolean isSimpleIncluded(String propName) {
        if (Utilities.listHasElements(this.toInclude as ArrayList<?>)) {
            for (String includePropName : this.toInclude) {
                if (propName == includePropName) return true
            }
        }
        return false
    }

    private boolean iscompositionIncluded(String propName) {
        if (Utilities.listHasElements(this.toInclude as ArrayList<?>)) {
            for (int j = 0; this.toInclude.size(); j ++) {
                if (isComposition(this.toInclude[j])) {
                    String[] exploded = this.toInclude[j].split(java.util.regex.Pattern.quote("."))
                    System.out.println(exploded)
                    //List<String> compositionList = Utilities.splitString(includePropName, ".")
                    List<String> compositionList = new ArrayList<>()
                    for (int i=0; i < exploded.length; i++) {
                        compositionList.add(exploded[i]);
                    }
                    if (propName == compositionList.get(0)) {
                        compositionList.remove(0)
                        this.toInclude[j] = Utilities.excludeProcessedProp(compositionList)
                        return true
                    }
                }
            }
        }
        return false
    }

    private boolean isComposition(String s) {
        return s.contains(".")
    }
    /**
     * A simple object is one that can be set directly as the value of a JSON property, examples include strings,
     * numbers, booleans, etc.
     *
     * @param propValue
     * @return
     */
    private boolean isSimple(propValue) {
        // This is a bit simplistic as an object might very well be Serializable but have properties that we want
        // to render in JSON as a nested object. If we run into this issue, replace the test below with an test
        // for whether propValue is an instanceof Number, String, Boolean, Char, etc.
        !(propValue instanceof BaseEntity) && !(propValue instanceof Collection) // || propValue == null
    }

     static JSONArray setObjectListJsonResponse(List objectList) {
        JSONArray patientList = new JSONArray()

        for (object in objectList) {
            JSONObject jo = new JSONObject(new JSONSerializer(object).getJSON())
            patientList.add(jo)
        }

        return patientList
    }

    static JSONObject setJsonObjectResponse(Object object) {

        return new JSONObject(new JSONSerializer(object).getJSON())
    }

    static JSONObject setJsonLightObjectResponse(Object object, List<String> toInclude) {

        return new JSONObject(new JSONSerializer(object, true, toInclude).getJSON())
    }

    static JSONArray setLightObjectListJsonResponse(List objectList, List<String> toInclude) {
        JSONArray patientList = new JSONArray()

        for (object in objectList) {
            JSONObject jo = new JSONObject(new JSONSerializer(object, true, toInclude).getJSON())
            patientList.add(jo)
        }

        return patientList
    }


}
