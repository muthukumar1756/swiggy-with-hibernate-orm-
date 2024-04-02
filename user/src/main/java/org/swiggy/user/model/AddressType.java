package org.swiggy.user.model;

/**
 * <p>
 * Defines the type of address of the user.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public enum AddressType {
    Home(1),
    OFFICE(2);

    private final int id;

    AddressType(final int id) {
        this.id = id;
    }

    public static int getId(final AddressType addressType) {
        return addressType.id;
    }

    public static AddressType getTypeById(final int id) {
        for (AddressType type : values()) {

            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
