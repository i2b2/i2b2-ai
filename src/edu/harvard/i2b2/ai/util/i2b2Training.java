package edu.harvard.i2b2.ai.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javax.xml.bind.JAXBElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ItemType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ItemType.ConstrainByDate;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ItemType.ConstrainByValue;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.PanelType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.QueryDefinitionType;
import edu.harvard.i2b2.common.exception.I2B2DAOException;
import edu.harvard.i2b2.common.util.jaxb.JAXBUnWrapHelper;
import edu.harvard.i2b2.common.util.jaxb.JAXBUtilException;
import edu.harvard.i2b2.crc.opencsv.*;

public class i2b2Training {



	public static void main(String[] args) {

		try {
			// create Gson instance
			Gson gson = new Gson();

			// create a reader
			Reader reader = Files.newBufferedReader(Paths.get("/Users/mem61/Downloads/i2b2.json"));
			BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/mem61/Downloads/llm_i2b2train.json"));


			// convert JSON array to list of users
			List<TrainingType> users = new Gson().fromJson(reader, new TypeToken<List<TrainingType>>() {}.getType());

			// print users
			for (TrainingType ttype: users)

			{

				ttype.setOutput(ttype.getOutput().substring(7));
				ttype.setOutput(ttype.getOutput().substring(0,ttype.getOutput().length()-3));
				QueryDefinitionType queryFromAI =  getQueryDefinitionType(ttype.getOutput());

				if (queryFromAI == null)
					continue;

				//QueryDefinitionType newQuery = new QueryDefinitionType();

				String str = "";
				//            	String instr = "I want to find all patients that";
				boolean  skip = false;
				for(int k=0; k < queryFromAI.getPanel().size(); k++)
					//            	for (PanelType panel: queryFromAI.getPanel())
				{
					PanelType panel = queryFromAI.getPanel().get(k);
					if (k > 0) {
						str += ("|");
						//      				instr += " and";
					}
					str +=  ("{");
					PanelType newPanel = new PanelType();
					newPanel.setPanelTiming(panel.getPanelTiming());
					newPanel.setInvert(panel.getInvert());

					//for(ItemType item: panel.getItem())
					for(int j=0; j < panel.getItem().size(); j++)
					{
						ItemType item = panel.getItem().get(j);
						if (j > 0)
						{
							str += ("|");
							//        				instr += " or";
						}
						if (item.getItemKey().contains("BIRNBIRN"))
							skip = true;
						else if (item.getItemKey().contains("masterid:"))
							skip = true;

						//            			instr += item.getItemName();
						//ItemType newItem = new ItemType();
						//newItem.setItemName(item.getItemName());
						//item.setItemIsSynonym(null);
						//		if (newItem.getItemName() != null)
						//			newPanel.getItem().add(newItem);
						str += (item.getItemName());
						if (item.getConstrainByValue() != null)
						{
							for (ConstrainByValue cbValue: item.getConstrainByValue())
							{

								str += "~VALUE:" + cbValue.getValueOperator() + "|";

								str += cbValue.getValueConstraint()  + "|" + cbValue.getValueUnitOfMeasure();
							}
						}
						if (item.getConstrainByDate() != null)
						{
							for (ConstrainByDate cbDate: item.getConstrainByDate())
							{
								if ((cbDate.getDateFrom() != null) && (cbDate.getDateFrom().getValue() != null))
									str += "~DATEFROM:" + cbDate.getDateFrom().getValue().toXMLFormat() + " ";
								if ((cbDate.getDateTo() != null) && (cbDate.getDateTo().getValue() != null))
									str += "~DATETO:" + cbDate.getDateTo().getValue().toXMLFormat() + " ";

							}
						}
					}
					str += ("}");
					///	if (newPanel.getItem() != null)
					//		newQuery.getPanel().add(newPanel);
				}
				//str += "\n";
				TrainingType ttypes = new TrainingType();
				ttypes.setOutput(str);
				ttypes.setInstruction("ENTER i2b2 MODE: " + ttype.getInstruction().substring(1));
				if (skip == false) {
					writer.write(gson.toJson( ttypes) + ",\n");

				}

			}
			// close reader
			reader.close();
			writer.close();
			/*
		    // convert JSON file to map
		    Map<?, ?> map = gson.fromJson(reader, Map.class);

		    // print map entries
		    for (Map.Entry<?, ?> entry : map.entrySet()) {
		        System.out.println(entry.getKey() + "=" + entry.getValue());
		    }

		    // close reader
		    reader.close();
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}



	public static void mainParseXML(String[] args) {
		// TODO Auto-generated method stub


		System.out.println("[");
		System.out.println("{");

		FileInputStream is;
		try {
			is = new FileInputStream("/Users/mem61/Downloads/query.csv");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader buffReader = new BufferedReader(isr);

			// use own CSVParser to set separator
			final CSVParser parser = new CSVParserBuilder()
					.withSeparator(',')
					.build();

			// use own CSVReader make use of own CSVParser
			CSVReader reader = new CSVReaderBuilder(buffReader)
					.withCSVParser(parser)
					.build();
			List<List<String>> rows = new ArrayList<>();
			List<String> xmlquery = new ArrayList();
			int i=1;
			for (String[] nextLine : reader) {
				if (i == 3) {
					xmlquery.add(nextLine[2]);
					i=1;
				}
				i++;
				rows.add(new ArrayList<>(Arrays.asList(nextLine)));
			}
			reader.close();




			// now go through each and create a json 
			for (String xmlq: xmlquery)
			{
				Gson gson = new GsonBuilder().setPrettyPrinting()
						.create();

				QueryDefinitionType queryFromAI =  getQueryDefinitionType(xmlq);

				//QueryDefinitionType newQuery = new QueryDefinitionType();

				String str = "";
				String instr = "I want to find all patients that";
				boolean  skip = false;
				for(int k=0; k < queryFromAI.getPanel().size(); k++)
					//            	for (PanelType panel: queryFromAI.getPanel())
				{
					PanelType panel = queryFromAI.getPanel().get(k);
					if (k > 0) {
						str += ("|");
						instr += " and";
					}
					str +=  ("{");
					PanelType newPanel = new PanelType();
					newPanel.setPanelTiming(panel.getPanelTiming());
					newPanel.setInvert(panel.getInvert());

					//for(ItemType item: panel.getItem())
					for(int j=0; j < panel.getItem().size(); j++)
					{
						ItemType item = panel.getItem().get(j);
						if (j > 0)
						{
							str += ("|");
							instr += " or";
						}
						if (item.getItemKey().contains("i2b2_DIAG") || item.getItemKey().contains("ICD10_ICD9")) 
							instr += " has a diagnosis of ";
						else if (item.getItemKey().contains("ICD10_ICD9")) 
							instr += " has a condition of ";
						else if (item.getItemKey().contains("i2b2_MEDS"))
							instr += " has taking a medicine of ";
						else if (item.getItemKey().contains("i2b2_PROC"))
							instr += " has procedure done ";
						else if (item.getItemKey().contains("i2b2_LABS"))
							instr += " has a lab of "; 
						else if (item.getItemKey().contains("BIRNBIRN"))
							skip = true;
						else if (item.getItemKey().contains("masterid:"))
							skip = true;
						else if (item.getItemKey().contains("DemographicsGender"))
							instr += " is a ";
						else if (item.getItemKey().contains("DemographicsAge"))
							instr += " is ";
						else if (item.getItemKey().contains("DemographicsReligion"))
							instr += " is a ";
						else if (item.getItemKey().contains("DemographicsLanguage"))
							instr += " speaks ";
						else 
							skip = true; //instr += item.getItemKey();

						instr += item.getItemName();
						//ItemType newItem = new ItemType();
						//newItem.setItemName(item.getItemName());
						//item.setItemIsSynonym(null);
						//		if (newItem.getItemName() != null)
						//			newPanel.getItem().add(newItem);
						str += (item.getItemName());
					}
					str += ("}");
					///	if (newPanel.getItem() != null)
					//		newQuery.getPanel().add(newPanel);
				}
				//str += "\n";
				TrainingType ttype = new TrainingType();
				ttype.setOutput(str);
				ttype.setInstruction(instr);
				if (skip == false) {
					System.out.println(gson.toJson( ttype) + ",");

				}
				//if  (newQuery.getPanel() != null)
				//System.out.println(gson.toJson( newQuery));
			}


			System.out.println("]");




		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static QueryDefinitionType  getQueryDefinitionType(String requestString) throws I2B2DAOException { 
		QueryDefinitionType queryDefReqType = null;
		JAXBElement responseJaxb;
		try {
			responseJaxb = AIJAXBUtil.getJAXBUtil()
					.unMashallFromString(requestString);

			queryDefReqType = (QueryDefinitionType) responseJaxb.getValue();
			//BodyType bodyType = r.getMessageBody();
			// 	get body and search for analysis definition
			JAXBUnWrapHelper unWraphHelper = new JAXBUnWrapHelper();


			//queryDefReqType = (QueryDefinitionRequestType) unWraphHelper
			//		.getObjectByClass(bodyType.getAny(),
			//				QueryDefinitionRequestType.class);
		} catch (JAXBUtilException e) {
			//throw new I2B2DAOException(e.getMessage());
		}

		return queryDefReqType;
	}
}
