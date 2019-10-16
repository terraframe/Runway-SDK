package com.runwaysdk;

public class AttributeUUIDParseException extends AttributeParseException
{
  /**
   * 
   */
  private static final long serialVersionUID = -6949542477209859629L;

  /**
   * Constructs a new <code>AttributeUUIDParseException</code> with the specified developer message
   * and a default business message. Leaving the default business message is
   * discouraged, as it provides no context information for end users.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param attributeDisplayLabel
   * @param invalidValue
   */
  public AttributeUUIDParseException(String devMessage, String attributeDisplayLabel, String invalidValue)
  {
    super(devMessage, attributeDisplayLabel, invalidValue);
  }

  /**
   * Constructs a new <code>AttributeCharacterParseException</code> with the specified detail message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this <code>RunwayException</code>'s detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param attributeDisplayLabel
   * @param invalidValue
   */
  public AttributeUUIDParseException(String devMessage, Throwable cause, String attributeDisplayLabel, String invalidValue)
  {
    super(devMessage, cause, attributeDisplayLabel, invalidValue);
  }

  /**
   * Constructs a new <code>AttributeCharacterParseException</code> with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * <code>RunwayException</code> is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param attributeDisplayLabel
   * @param invalidValue
   */
  public AttributeUUIDParseException(Throwable cause, String attributeDisplayLabel, String invalidValue)
  {
    super(cause, attributeDisplayLabel, invalidValue);
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  { 
    return CommonExceptionMessageLocalizer.uuidParseException(this.getLocale(), this.getAttributeDisplayLabel(), this.getInvalidValue());
  }
}
