package it.cineca.surplus.ir.crosswalks;

import java.util.Map;

import org.dspace.content.DCValue;
import org.dspace.content.Item;

/**
 * Implements virtual field processing for split pagenumber range information.
 * 
 * @author bollini
 */
public class VirtualFieldPageNumber implements VirtualFieldDisseminator, VirtualFieldIngester {
	public String[] getMetadata(Item item, Map<String, String> fieldCache, String fieldName) {
		// Check to see if the virtual field is already in the cache
		// - processing is quite intensive, so we generate all the values on
		// first request
		if (fieldCache.containsKey(fieldName))
			return new String[] { fieldCache.get(fieldName) };

		String[] virtualFieldName = fieldName.split("\\.");

		String qualifier = virtualFieldName[2];
		String separator = " - ";
		if (qualifier.equals("bibtex")) {
			separator = "--";
		}
		// Get the citation from the item
		DCValue[] dcvs = item.getMetadata("dc.relation.firstpage");
		DCValue[] dcvs2 = item.getMetadata("dc.relation.lastpage");

		if ((dcvs != null && dcvs.length > 0) && (dcvs2 != null && dcvs2.length > 0)) {
			String value = dcvs[0].value + separator + dcvs2[0].value;
			fieldCache.put(fieldName, value);
			return new String[] { value };
		}

		return null;
	}

	public boolean addMetadata(Item item, Map<String, String> fieldCache, String fieldName, String value) {
		// NOOP - we won't add any metadata yet, we'll pick it up when we
		// finalise the item
		return true;
	}

	public boolean finalizeItem(Item item, Map<String, String> fieldCache) {
		return false;
	}
}