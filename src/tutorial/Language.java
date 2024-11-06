/**
 * Automatically generated by Zserio Java generator version 1.1.2 using Zserio core 2.15.0.
 * Generator setup: writerCode, pubsubCode, serviceCode, sqlCode.
 */

package tutorial;

public enum Language implements zserio.runtime.io.Writer, zserio.runtime.SizeOf,
        zserio.runtime.ZserioEnum
{
    CPP((byte)0),
    JAVA((byte)1),
    PYTHON((byte)2),
    JS((byte)3);

    private Language(byte value)
    {
        this.value = value;
    }

    public byte getValue()
    {
        return value;
    }

    @Override
    public java.lang.Number getGenericValue()
    {
        return value;
    }

    @Override
    public int bitSizeOf()
    {
        return bitSizeOf(0);
    }

    @Override
    public int bitSizeOf(long bitPosition)
    {
        return 2;
    }

    @Override
    public long initializeOffsets()
    {
        return initializeOffsets(0);
    }

    @Override
    public long initializeOffsets(long bitPosition) throws zserio.runtime.ZserioError
    {
        return bitPosition + bitSizeOf(bitPosition);
    }

    @Override
    public void write(zserio.runtime.io.BitStreamWriter out) throws java.io.IOException
    {
        out.writeBits(getValue(), 2);
    }

    public static Language readEnum(zserio.runtime.io.BitStreamReader in) throws java.io.IOException
    {
        return toEnum((byte)in.readBits(2));
    }

    public static Language toEnum(byte value)
    {
        switch (value)
        {
            case (byte)0:
                return CPP;
            case (byte)1:
                return JAVA;
            case (byte)2:
                return PYTHON;
            case (byte)3:
                return JS;
            default:
                throw new java.lang.IllegalArgumentException(
                        "Unknown value for enumeration Language: " + value + "!");
        }
    }

    public static Language toEnum(java.lang.String itemName)
    {
        if (itemName.equals("CPP"))
            return CPP;
        if (itemName.equals("JAVA"))
            return JAVA;
        if (itemName.equals("PYTHON"))
            return PYTHON;
        if (itemName.equals("JS"))
            return JS;
        throw new java.lang.IllegalArgumentException(
                "Enum item '" + itemName + "' doesn't exist in enumeration Language!");
    }

    private byte value;
}
