package com.groupbyinc.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupbyinc.api.parser.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>
 * A template is always returned. By default an empty template called
 * 'default' will be returned. If a rule is triggered the corresponding template
 * will be returned with the corresponding zones populated with the correct
 * content.
 * A template has the following properties
 * <p/>
 * - id: an MD5 of the name
 * - name: the name of this template.
 * - zones: a list of the zones for this template.
 * - zonesByName: a map of the zones by name.
 * <p/>
 * </code>
 *
 * @author will
 */
public class Template extends Model {
    private static final long serialVersionUID = 1L;
    @JsonProperty("_id")
    private String id;
    private String name;
    private String ruleName;
    private List<Zone> zones;
    private Map<String, Zone> zonesByName = new HashMap<String, Zone>();

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public Template() {
        // default constructor
    }

    /**
     * @return An MD5 hash of the name of this template.
     */
    public String getId() {
        return id;
    }

    /**
     * @param pId
     *         Set the ID.
     *
     * @return
     */
    public Template setId(String pId) {
        id = pId;
        return this;
    }

    /**
     * @return The name as set in the command center.
     */
    public String getName() {
        return name;
    }

    /**
     * @param pName
     *         Set the name
     *
     * @return
     */
    public Template setName(String pName) {
        name = pName;
        return this;
    }

    /**
     * @return The name of the rule that triggered this template.
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * @param pRuleName
     *         Set the rule.
     */
    public void setRuleName(String pRuleName) {
        ruleName = pRuleName;
    }

    /**
     * @return A list of the zones in this template.
     */
    public List<Zone> getZones() {
        return zones;
    }

    /**
     * @param pZones
     *         Set the zones.
     *
     * @return
     */
    public Template setZones(List<Zone> pZones) {
        zones = pZones;
        if (zones != null) {
            for (Zone zone : zones) {
                zonesByName.put(zone.getName(), zone);
            }
        }
        return this;
    }

    /**
     * @return A map of zones keyed by name for easy lookup in the UI layer.
     */
    public Map<String, Zone> getZonesByName() {
        return zonesByName;
    }
}
