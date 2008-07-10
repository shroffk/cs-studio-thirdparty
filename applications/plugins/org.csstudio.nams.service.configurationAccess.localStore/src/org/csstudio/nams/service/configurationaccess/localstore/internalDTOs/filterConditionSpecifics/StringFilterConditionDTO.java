package org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.filterConditionSpecifics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.csstudio.nams.common.fachwert.MessageKeyEnum;
import org.csstudio.nams.common.material.regelwerk.StringRegelOperator;
import org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.FilterConditionDTO;

/**
 * Dieses Daten-Transfer-Objekt stellt hält die Konfiguration einer
 * AMS_FilterCondition_String.
 * 
 * Das Create-Statement für die Datenbank hat folgendes Aussehen:
 * 
 * <pre>
 *  create table AMS_FilterCondition_String
 *  (
 *  iFilterConditionRef	INT NOT NULL,
 *  cKeyValue		VARCHAR(16),
 *  sOperator		SMALLINT,
 *  cCompValue		VARCHAR(128)
 *  );
 * </pre>
 */
@Entity
@Table(name = "AMS_FilterCondition_String")
@PrimaryKeyJoinColumn(name = "iFilterConditionRef", referencedColumnName="iFilterConditionID")
public class StringFilterConditionDTO extends FilterConditionDTO {

	@SuppressWarnings("unused")
	protected void setFilterConditionTypeRef(int typeRef){
		super.filterCondtionTypeRef = 1;
	}
	public int getFilterConditionTypeRef(){
		return filterCondtionTypeRef;
	}
	
	@Column(name = "iFilterConditionRef", nullable = false, updatable = false, insertable = false)
	private int iFilterConditionRef;

	@Column(name = "cKeyValue", length = 16)
	private String keyValue;

	@Column(name = "sOperator")
	private short operator;

	@Column(name = "cCompValue", length = 128)
	private String compValue;

	/**
	 * @return the filterConditionRef
	 */
	@SuppressWarnings("unused")
	private int getIFilterConditionRef() {
		return iFilterConditionRef;
	}

	/**
	 * @param filterConditionRef
	 *            the filterConditionRef to set
	 */
	@SuppressWarnings("unused")
	private void setIFilterConditionRef(int filterConditionRef) {
		this.iFilterConditionRef = filterConditionRef;
	}

	/**
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

	public MessageKeyEnum getKeyValueEnum() {
		MessageKeyEnum valueOf = MessageKeyEnum.valueOf(keyValue);
		return valueOf;
	}

	/**
	 * @param keyValue
	 *            the keyValue to set
	 */
	protected void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	
	public void setKeyValue(MessageKeyEnum keyValue){
		this.keyValue = keyValue.getStringValue();
	}

	/**
	 * @return the operator
	 */
	@SuppressWarnings("unused")
	public short getOperator() {
		return operator;
	}

	public StringRegelOperator getOperatorEnum(){
		return StringRegelOperator.valueOf(operator);
	}
	
	/**
	 * TODO Rename to sth. like setStringOperator
	 */
	public void setOperatorEnum(StringRegelOperator op){
		operator = (short) op.ordinal();
	}
	
	/**
	 * @param operator
	 *            the operator to set
	 */
	private void setOperator(short operator) {
		this.operator = operator;
	}

	/**
	 * @return the compValue
	 */
	@SuppressWarnings("unused")
	public String getCompValue() {
		return compValue;
	}

	/**
	 * @param compValue
	 *            the compValue to set
	 */
	public void setCompValue(String compValue) {
		this.compValue = compValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append(" + key: ");
		builder.append(this.keyValue);
		builder.append(", operator: ");
		builder.append(this.operator);
		builder.append(", compareValue: ");
		builder.append(this.compValue);
		return builder.toString();
	}
}
