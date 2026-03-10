package utils;

/**
 * Representation of a packet
 * @param hash An SHA-256 hash of the text passed in to this packet
 * @param previous_hash An SHA-256 hash of the previous packet sent. Can be {@code null}
 * @param packetNum Which packet this is in the packet chain
 * @param totalPackets How many packets there are in the packet chain
 * @param text Portion of the message you're sending. Hadrian uses this for sending XML files. Must be UTF-8 compliant
 */
public record PacketHeader(byte[] hash, byte[] previous_hash, int packetNum, int totalPackets, String text){}