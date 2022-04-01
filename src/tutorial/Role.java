/**
 * Automatically generated by Zserio Java extension version 2.5.1.
 * Generator setup: writerCode, pubsubCode, serviceCode, sqlCode.
 */

package tutorial;

public enum Role implements zserio.runtime.io.InitializeOffsetsWriter,
        zserio.runtime.SizeOf, zserio.runtime.ZserioEnum
{
    DEVELOPER((short)0),
    TEAM_LEAD((short)1),
    CTO((short)2);

    private Role(short value)
    {
        this.value = value;
    }

    public short getValue()
    {
        return value;
    }

    @Override
    public java.lang.Number getGenericValue()
    {
        return value;
    }

    public static void createPackingContext(zserio.runtime.array.PackingContextNode contextNode)
    {
        contextNode.createContext();
    }

    @Override
    public void initPackingContext(zserio.runtime.array.PackingContextNode contextNode)
    {
        contextNode.getContext().init(
                new zserio.runtime.array.ArrayTraits.BitFieldShortArrayTraits(8),
                new zserio.runtime.array.ArrayElement.ShortArrayElement(value));
    }

    @Override
    public int bitSizeOf()
    {
        return bitSizeOf(0);
    }

    @Override
    public int bitSizeOf(long bitPosition)
    {
        return 8;
    }

    @Override
    public int bitSizeOf(zserio.runtime.array.PackingContextNode contextNode, long bitPosition)
    {
        return contextNode.getContext().bitSizeOf(
                new zserio.runtime.array.ArrayTraits.BitFieldShortArrayTraits(8),
                new zserio.runtime.array.ArrayElement.ShortArrayElement(value));
    }

    @Override
    public long initializeOffsets(long bitPosition) throws zserio.runtime.ZserioError
    {
        return bitPosition + bitSizeOf(bitPosition);
    }

    @Override
    public long initializeOffsets(zserio.runtime.array.PackingContextNode contextNode, long bitPosition)
    {
        return bitPosition + bitSizeOf(contextNode, bitPosition);
    }

    @Override
    public void write(zserio.runtime.io.BitStreamWriter out) throws java.io.IOException
    {
        write(out, false);
    }

    @Override
    public void write(zserio.runtime.io.BitStreamWriter out, boolean callInitializeOffsets)
            throws java.io.IOException
    {
        out.writeUnsignedByte(getValue());
    }
    
    @Override
    public void write(zserio.runtime.array.PackingContextNode contextNode,
            zserio.runtime.io.BitStreamWriter out) throws java.io.IOException
    {
        contextNode.getContext().write(
                new zserio.runtime.array.ArrayTraits.BitFieldShortArrayTraits(8), out,
                new zserio.runtime.array.ArrayElement.ShortArrayElement(value));
    }

    public static Role readEnum(zserio.runtime.io.BitStreamReader in) throws java.io.IOException
    {
        return toEnum(in.readUnsignedByte());
    }

    public static Role readEnum(zserio.runtime.array.PackingContextNode contextNode,
            zserio.runtime.io.BitStreamReader in) throws java.io.IOException
    {
        return toEnum(((zserio.runtime.array.ArrayElement.ShortArrayElement)
                contextNode.getContext().read(
                        new zserio.runtime.array.ArrayTraits.BitFieldShortArrayTraits(8), in)).get());
    }

    public static Role toEnum(short value)
    {
        switch (value)
        {
            case (short)0:
                return DEVELOPER;
            case (short)1:
                return TEAM_LEAD;
            case (short)2:
                return CTO;
            default:
                throw new java.lang.IllegalArgumentException(
                        "Unknown value for enumeration Role: " + value + "!");
        }
    }

    private short value;
}
