package cs2420;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Helper methods for dealing with bytes and bits
 * 
 * @author H. James de St. Germain
 *
 */
public final class Bit_Operations
{

	/**
	 * this is a "static" class (all methods are static), don't allow one to be created
	 */
	private Bit_Operations()
	{
		;
	}
	
	/**
	 * 
	 * Return a byte array representing the bitset data.
	 * 
	 * This method converts a bitset to a list of bytes. To complicate matters,
	 * because BitSets use LITTLE ENDIAN notation, we have to first convert the
	 * bits to BIG ENDIAN (the way we want the bits), and then output as bytes
	 * 
	 * Here is what that means:
	 * 
	 *   if our bit stream is as follows (1 1 0 0 0 0 0 0) and you just use
	 *   bitset.toByteArray, the output byte would be: (0 0 0 0 0 0 1 1)
	 * 
	 * To swap from little to big, you must reverse the order of the bits
	 * on a byte by byte basis.  Thus if you have the bit set:
	 *
	 *    0 1 2 3 4 5 6 7 8 9 a b c d e f
	 *    -------------------------------
	 *    1 0 1 1 0 1 1 1 0 0 0 0 0 1 1 0
	 *    
	 * which is two bytes
	 * 
	 *    0 1 2 3 4 5 6 7   0 1 2 3 4 5 6 7
	 *    ---------------   ---------------
	 *    1 0 1 1 0 1 1 1 | 0 0 0 0 0 1 1 0
	 *    
	 * we reverse the first byte and then the second giving us
	 * 
	 *    1 1 1 0 1 1 0 1 | 0 1 1 0 0 0 0 0
	 *    
	 * then when we dump the byteArray, these will be again reversed and we get what we expected
	 *    
	 * 
	 * tldr; For each 8 bits (byte) in the bit set, reverse the order of those bits.
	 * 
	 * @param bits - the bitset to convert from little endian to big endian
	 * 
	 * @return the new bitset
	 */

	public static byte[] get_bytes ( BitSet bits )
	{
		// 1) Create a new bit set: RESULT
		// 2) for every bit in the bit set
		//       if divisible by 8 we are at a new byte, so remember this fact
		//       if the bit is true, set the appropriate bit in RESULT 
		// 3) return a byte array from the RESULT 
		BitSet result = new BitSet(bits.length());
		int largeBucket = ((bits.length() - 1) / 8) + 1;
		int smallBucket = ((bits.length() - 1) / 8);
		int bucket = bits.length() - 1 == smallBucket ? smallBucket : largeBucket;
		for(int bucketIndex = bucket; bucketIndex > 0; bucketIndex--){
			for(int bitOffset = 0; bitOffset < 8; bitOffset++){
				if(bits.get((bucketIndex * 8) - 1 - bitOffset)){
					result.set((bucketIndex - 1) * 8 + bitOffset);
					
				}
			}
		}
		return result.toByteArray();
	}

	/**
	 * convert an integer into 4 bytes
	 * 
	 * @param value - an integer value
	 * @return the four bytes that make up the integer
	 */
	public static byte[] convert_integer_to_bytes( int value )
	{
		ByteBuffer int_to_byte_converter = ByteBuffer.allocate(4);
		
		int_to_byte_converter.putInt( value );
		return int_to_byte_converter.array();
	}
	
	/**
	 * Get a single bit out of an 8 bit byte
	 * 
	 * remember the bit sequence for the number 7 is:  00000111
	 *                                      indexes:   01234567
	 * get_bit(7,0) --> false
	 * get_bit(7,1) --> false
	 * get_bit(7,2) --> false
	 * get_bit(7,3) --> false
	 * get_bit(7,4) --> false
	 * get_bit(7,5) --> true
	 * get_bit(7,6) --> true
	 * get_bit(7,7) --> true
	 * 
	 * @param the_byte 
	 * @param position - 0 to 7  with 0 being on the left and 7 being on the right
	 * @return the bit as a boolean
	 */
	public static boolean get_bit( byte the_byte, int position)
	{
		return ((0b0000_0001 << (7 - position)) & the_byte) != 0;
	}

}
