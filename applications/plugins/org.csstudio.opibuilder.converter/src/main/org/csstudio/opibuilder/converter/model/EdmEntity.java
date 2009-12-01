package org.csstudio.opibuilder.converter.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Generic data container for Edm widget or group.
 * Base class for all specific Edm widget classes.
 * 
 * @author Matevz
 *
 */
public class EdmEntity extends Object {

	private static Logger log = Logger.getLogger("org.csstudio.opibuilder.converter.model.EdmEntity");

	private Map<String, EdmAttribute>	attributeMap;
	private Vector<EdmEntity>			subEntities;
	private String						type;

	/**
	 * Constructs an empty EdmEntity of type specified.
	 * @param type EdmEntity type.
	 */
	public EdmEntity(String type) {
		attributeMap = new HashMap<String, EdmAttribute>();
		subEntities = new Vector<EdmEntity>();
		this.type = type;
	}

	/**
	 * Constructs an instance of EdmEntity from data of another EdmEntity instance.
	 * If given EdmEntity is an extended Edm class, its annotated properties will
	 * be parsed and replaced with specific extensions of EdmAttribute and EdmWidget
	 * instances. 
	 * 
	 * @param copy EdmEntity to copy.
	 * @throws EdmException if there is a parsing error.
	 */
	public EdmEntity(EdmEntity copy) throws EdmException {

		attributeMap = new HashMap<String, EdmAttribute>();
		subEntities = new Vector<EdmEntity>();
		this.type = copy.type;

		for (String key : copy.getAttributeIdSet())
			attributeMap.put(key, copy.getAttribute(key));

		for (int i = 0; i < copy.getSubEntityCount(); i++)
			subEntities.add(new EdmEntity(copy.getSubEntity(i)));

		String className = this.getClass().getName();
		Logger log = Logger.getLogger(className);
		log.debug("Parsing specific " + className + ".");

		try {
			for (Field f : this.getClass().getDeclaredFields()) {
				if (f.isAnnotationPresent(EdmAttributeAn.class)) {

					// new, specific attribute id(name) & instance
					String			name = f.getName();
					EdmAttribute	a = null;
					boolean			isEdmAttribute = true;	// if attribute has been initialized

					boolean required = true;
					if (f.isAnnotationPresent(EdmOptionalAn.class)) {
						log.debug("Parsing optional property: " + name);
						required = false;
					}
					else
						log.debug("Parsing required property: " + name);


					f.setAccessible(true);

					if (f.getType().equals(EdmInt.class))
						a = new EdmInt(getAttribute(name), required);

					else if (f.getType().equals(int.class)) {
						EdmInt i = new EdmInt(getAttribute(name), required);
						setAttribute(name, i);
						f.set(this, i.get());
						isEdmAttribute = false;
					}

					else if (f.getType().equals(EdmString.class))
						a = new EdmString(getAttribute(name), required);

					else if (f.getType().equals(String.class)) {
						EdmString s = new EdmString(getAttribute(name), required);
						setAttribute(name, s);
						f.set(this, s.get());
						isEdmAttribute = false;
					}

					else if (f.getType().equals(EdmColor.class))
						a = new EdmColor(getAttribute(name), required);//EdmColor.parseColor(this, name);

					else if (f.getType().equals(EdmFont.class))
						a = new EdmFont(getAttribute(name), required);

					else if (f.getType().equals(EdmBoolean.class))
						a =  new EdmBoolean(getAttribute(name));

					else if (f.getType().equals(boolean.class)) {
						EdmBoolean b = new EdmBoolean(getAttribute(name));
						setAttribute(name, b);
						f.set(this, b.is());
						isEdmAttribute = false;
					}
					else if (f.getType().equals(EdmLineStyle.class))
						a =  new EdmLineStyle(getAttribute(name), required);

					else if (f.getType().equals(Vector.class)) {
						f.set(this, parseWidgets());
						isEdmAttribute = false;
					}

					else {
						isEdmAttribute = false;
						log.warn("Property type not mapped!");
					}


					if (isEdmAttribute) {
						setAttribute(name, a);
						f.set(this, a);
					}

					f.setAccessible(false);
				}
			}
		} catch (Exception e) {
			if (e instanceof EdmException)
				throw (EdmException)e;
			else {
				e.printStackTrace();
				throw new EdmException(EdmException.SPECIFIC_PARSING_ERROR,
				"Error when parsing annotated field.");
			}
		}
	}

	/**
	 * Returns the number of attributes in entity.
	 * @return	The number of attributes in entity.
	 */
	public int getAttributeCount() {
		return attributeMap.size();
	}

	/**
	 * Adds the attribute with specified id to entity.
	 * Id must be unique.
	 * @param id	Unique id of attribute.
	 * @param a		Attribute to add.
	 * @throws EdmException 
	 */
	public void addAttribute(String id, EdmAttribute a) throws EdmException {
		if (attributeMap.containsKey(id))
			throw new EdmException(EdmException.ATTRIBUTE_ALREADY_EXISTS,
					"Attribute " + id + " already exists.");
		else
			attributeMap.put(id, a);
	}

	/**
	 * Returns the attribute with specified id.
	 * @param id	The id of attribute that we want to get.
	 * @return		Attribute with desired id.
	 */
	public EdmAttribute getAttribute(String id) {
		return attributeMap.get(id);
	}

	/**
	 * Returns all names of EdmAttributes in current EdmEntity.
	 * @return Names of all EdmAttributes (keys in HashMap).
	 */
	public Set<String> getAttributeIdSet() {
		return attributeMap.keySet();
	}

	/**
	 * Returns the number of subentities in an entity. 
	 * @return	The number of subentities.
	 */
	public int getSubEntityCount() {
		return subEntities.size();
	}

	/**
	 * Adds subentity to an entity.
	 * @param subE	Subentity to add.
	 */
	public void addSubEntity(EdmEntity subE) {
		subEntities.add(subE);
	}

	/**
	 * Returns the subentity at specified index.
	 * @param i	Index of desired subentity.
	 * @return	Subentity.
	 */
	public EdmEntity getSubEntity(int i) {
		return subEntities.get(i);
	}

	/**
	 * Changes the attribute with specified id.
	 * @param id		Id of the attribute that we want to change.
	 * @param newAtt	New attribute.
	 */
	protected void setAttribute(String id, EdmAttribute newAtt) {
		attributeMap.put(id, newAtt);
	}

	/**
	 * Changes the subentity at desired index.
	 * @param i		Index of the subentity to change.
	 * @param subE	New subentity.
	 */
	public void setSubEntity(int i, EdmEntity subE) {
		subEntities.set(i, subE);
	}

	/**
	 * Returns EdmEntity type.
	 * @return EdmEntity type.
	 */
	public String getType() {
		return type;
	}


	/**
	 * Parses specific widget data from its parent EdmEntity.
	 *
	 * @return a vector of parsed EdmWidgets in given EdmEntity.
	 * @throws EdmException there is an invalid data.
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	private Vector<EdmEntity> parseWidgets()  {

		boolean robust = Boolean.parseBoolean(System.getProperty("edm2xml.robustParsing"));

		Vector<EdmEntity> w = new Vector<EdmEntity>();

		log.debug("Parsing specific widgets.");

		String packageName = EdmWidget.class.getPackage().getName();

		for (int i = 0; i < subEntities.size(); i++) {
			EdmEntity subE = subEntities.get(i);
			String wType = "Edm_" + subE.getType();

			log.debug("Parsing specific widget: " + wType);

			Object o;
			try {
				o = Class.forName(packageName + "." + wType).
				getConstructor(EdmEntity.class).newInstance(subE);
				if (o instanceof EdmEntity) {
					subEntities.set(i, (EdmEntity)o);
					w.add((EdmEntity)o);
				} else {
					log.warn("Class not declared: " + wType);
				}
			} catch (ClassNotFoundException e) {
				log.warn("Class not declared: " + wType);
			} catch (Exception e) {
				if (!robust) {
					e.printStackTrace();
				}
			}
		}
		return w;
	}
}
