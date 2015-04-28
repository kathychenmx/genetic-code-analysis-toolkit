package de.hsma.gentool.nucleic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum Compound {
	START('\u25B6', "Start", null, new String[]{"AUG", "ATG"}),
	STOP('\u25CF', "Stop", null, new String[]{"UAA", "TAA", "UGA", "TGA", "UAG", "TAG"}),
	
	ALANINE('A', "Ala", Property.NONPOLAR, new String[]{"GCU", "GCT", "GCC", "GCA", "GCG"}),
	ARGININE('R', "Arg", Property.BASIC, new String[]{"CGU", "CGT", "CGC", "CGA", "CGG", "AGA", "AGG"}),
	ASPARAGINE('N', "Asn", Property.POLAR, new String[]{"AAU", "AAT", "AAC"}),
	ASPARTIC_ACID('D', "Asp", Property.ACIDIC, new String[]{"GAU", "GAT", "GAC"}),
	CYSTEINE('C', "Cys", Property.POLAR, new String[]{"UGU", "TGT", "UGC", "TGC"}),
	GLUTAMINE('Q', "Gln", Property.POLAR, new String[]{"CAA", "CAG"}),
	GLUTAMIC_ACID('E', "Glu", Property.ACIDIC, new String[]{"GAA", "GAG"}),
	GLYCINE('G', "Gly", Property.NONPOLAR, new String[]{"GGU", "GGT", "GGC", "GGA", "GGG"}),
	HISTIDINE('H', "His", Property.BASIC, new String[]{"CAU", "CAT", "CAC"}),
	ISOLEUCINE('I', "Ile", Property.NONPOLAR, new String[]{"AUU", "ATT", "AUC", "ATC", "AUA", "ATA"}),
	LEUCINE('L', "Leu", Property.NONPOLAR, new String[]{"UUA", "TTA", "UUG", "TTG", "CUU", "CTT", "CUC", "CTC", "CUA", "CTA", "CUG", "CTG"}),
	LYSINE('K', "Lys", Property.BASIC, new String[]{"AAA", "AAG"}),
	METHIONINE('M', "Met", Property.NONPOLAR, new String[]{"AUG", "ATG"}),
	PHENYLALANINE('F', "Phe", Property.NONPOLAR, new String[]{"UUU", "TTT", "UUC", "TTC"}),
	PROLINE('P', "Pro", Property.NONPOLAR, new String[]{"CCU", "CCT", "CCC", "CCA", "CCG"}),
	SERINE('S', "Ser", Property.POLAR, new String[]{"UCU", "TCT", "UCC", "TCC", "UCA", "TCA", "UCG", "TCG", "AGU", "AGT", "AGC"}),
	THREONINE('T', "Thr", Property.POLAR, new String[]{"ACU", "ACT", "ACC", "ACA", "ACG"}),
	TRYPTOPHAN('W', "Trp", Property.NONPOLAR, new String[]{"UGG", "TGG"}),
	TYROSINE('Y', "Tyr", Property.POLAR, new String[]{"UAU", "TAT", "UAC", "TAC"}),
	VALINE('V', "Val", Property.NONPOLAR, new String[]{"GUU", "GTT", "GUC", "GTC", "GUA", "GTA", "GUG", "GTG"}),
	
	UNKNOWN('?', "Unknown", null, (Collection<Tuple>)null);
	
	public enum Property {
		NONPOLAR, POLAR, BASIC, ACIDIC;
	}
	
	public final char letter;
	public final String abbreviation;
	public final Property property;
	public final Set<Tuple> tuples;
	
	private static Map<Tuple,Compound> tupleCompound;
	static {
		tupleCompound = new HashMap<Tuple,Compound>();
		for(Compound compound:Compound.values())
			if(!(compound.equals(START)||compound.equals(STOP)||compound.equals(UNKNOWN)))
				for(Tuple tuple:compound.tuples)
					tupleCompound.put(tuple,compound);
	}
	
	private Compound(char letter, String abbreviation, Property property, String[] strings) {
		this(letter, abbreviation, property, Tuple.splitTuples(strings));
	}
	private Compound(char letter, String abbreviation, Property property, Collection<Tuple> tuples) {
		this.letter = letter; this.abbreviation = abbreviation; this.property = property;
		this.tuples = tuples!=null?Collections.unmodifiableSet(new HashSet<Tuple>(tuples)):null;
	}
	
	public static boolean isStart(Tuple tuple) { return START.tuples.contains(tuple); }
	public static boolean isStop(Tuple tuple) { return STOP.tuples.contains(tuple); }
	public static Compound forTuple(Tuple tuple) {
		return tupleCompound.get(tuple);
	}
	
	@Override public String toString() {
		return abbreviation;
	}
}