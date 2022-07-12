package tk.smileyik.realsurvival.core.attribute.impl;

import tk.smileyik.realsurvival.core.attribute.RsAttribute;
import tk.smileyik.realsurvival.core.attribute.RsAttributeType;
import tk.smileyik.realsurvival.core.item.lore.LoreParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRsAttribute<T> implements RsAttribute<T> {
  private final String attributeId;
  private final String displayName;
  private final RsAttributeType attributeType;
  protected List<LoreParser<?>> loreParsers;
  protected Map<String, Object> properties;

  public AbstractRsAttribute(String attributeId,
                             String displayName,
                             RsAttributeType attributeType,
                             List<LoreParser<?>> loreParsers,
                             Map<String, Object> properties) {
    this.attributeId = attributeId;
    this.displayName = displayName;
    this.attributeType = attributeType;
    this.loreParsers = loreParsers;
    this.properties = properties;

    if (this.loreParsers == null) {
      this.loreParsers = new ArrayList<>();
    }
    if (this.properties == null) {
      this.properties = new HashMap<>();
    }
  }

  @Override
  public RsAttributeType getAttributeType() {
    return attributeType;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String getAttributeId() {
    return attributeId;
  }

  @Override
  public Map<String, Object> getProperties() {
    return properties;
  }

  @Override
  public Object getProperty(String key) {
    return properties.get(key);
  }

  @Override
  public void putProperty(String key, Object value) {
    properties.put(key, value);
  }
}
