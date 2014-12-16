package mao.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class LEDataInputStream
  implements DataInput
{
    private final DataInputStream dis;
    private final InputStream is;
    private final byte[] work;
    private int s,end;

  public LEDataInputStream(InputStream in)
  {
    is = in;
    dis = new DataInputStream(in);
    work = new byte[8];
  }

  @Override
public final boolean readBoolean()
    throws IOException
  {
    return dis.readBoolean();
  }

  @Override
public final byte readByte()
    throws IOException
  {
    return dis.readByte();
  }

  @Override
public final char readChar()
    throws IOException
  {
    dis.readFully(work, 0, 2);
    return (char)((work[1] & 0xFF) << 8 | work[0] & 0xFF);
  }

  @Override
public final double readDouble()
    throws IOException
  {
    return Double.longBitsToDouble(readLong());
  }

  public int[] readIntArray(int length) throws IOException {
    int[] array = new int[length];
    for (int i = 0; i < length; i++) {
      array[i] = readInt();
    }
    return array;
  }

  public void skipCheckInt(int expected) throws IOException {
    int got = readInt();
    if (got != expected)
      throw new IOException(String.format("Expected: 0x%08x, got: 0x%08x", new Object[] { Integer.valueOf(expected), Integer.valueOf(got) }));
  }

  public int read(byte[] b,int a,int len)throws IOException{
      return dis.read(b,a,len);
  }



  @Override
public final float readFloat()
    throws IOException
  {
    return Float.intBitsToFloat(readInt());
  }

  @Override
public final void readFully(byte[] ba)
    throws IOException
  {
    dis.readFully(ba, 0, ba.length);
  }

  @Override
public final void readFully(byte[] ba, int off, int len)
    throws IOException
  {
    dis.readFully(ba, off, len);
  }

  @Override
public final int readInt()
    throws IOException
  {
    dis.readFully(work, 0, 4);
    return work[3] << 24 | (work[2] & 0xFF) << 16 | (work[1] & 0xFF) << 8 | work[0] & 0xFF;
  }

  /** @deprecated */
  @Deprecated
@Override
public final String readLine()
    throws IOException
  {
    return dis.readLine();
  }

  @Override
public final long readLong()
    throws IOException
  {
    dis.readFully(work, 0, 8);
    return work[7] << 56 | (work[6] & 0xFF) << 48 | (work[5] & 0xFF) << 40 | (work[4] & 0xFF) << 32 | (work[3] & 0xFF) << 24 | (work[2] & 0xFF) << 16 | (work[1] & 0xFF) << 8 | work[0] & 0xFF;
  }

  @Override
public final short readShort()
    throws IOException
  {
    dis.readFully(work, 0, 2);
    return (short)((work[1] & 0xFF) << 8 | work[0] & 0xFF);
  }

  @Override
public final String readUTF()
    throws IOException
  {
    return dis.readUTF();
  }

  @Override
public final int readUnsignedByte()
    throws IOException
  {
    return dis.readUnsignedByte();
  }

  @Override
public final int readUnsignedShort()
    throws IOException
  {
    dis.readFully(work, 0, 2);
    return (work[1] & 0xFF) << 8 | work[0] & 0xFF;
  }

  @Override
public final int skipBytes(int n)
    throws IOException
  {
    return dis.skipBytes(n);
  }
}
