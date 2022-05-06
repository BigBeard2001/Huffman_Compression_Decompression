import java.io.*;
import java.util.*;

public class HuffmanDecompression {

	public static void main(String[] args) throws Exception {
		String compressedCode = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(args[0])));
		String[] huffmanCodes = HuffmanDecompression.ReadDict(args[1]);
		ArrayList<Nodes> list = HuffmanDecompression.GenerateNode(huffmanCodes);
		list = HuffmanDecompression.SortList(list);

		String outputText = HuffmanDecompression.Decode(compressedCode, list);
		
		FileWriter fwriter2 = new FileWriter(args[2], false);
		BufferedWriter bwriter2 = new BufferedWriter(fwriter2);
		if (outputText != null) bwriter2.write(outputText);
		bwriter2.flush();
		bwriter2.close();
		return;
	}

	public static String[] ReadDict(String path) throws FileNotFoundException {
		String[] huffmanCodes = new String[128];
		File file = new File(path);
		try {
			String line = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while ((line = reader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == ':') {
						int asc = Integer.parseInt(line.substring(0, i));
						String code = line.substring(i + 1, line.length());
						huffmanCodes[asc] = code;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return huffmanCodes;
	}

	public static String Decode(String compressedCode, ArrayList<Nodes> list) {
		String outputText = "";
		for (int i = 0; i < compressedCode.length(); i++) {
			for (int j = 0; j < list.size(); j++) {
				int subLength = list.get(j).code.length();
				String subCode = list.get(j).code;
				if (i + subLength - 1 < compressedCode.length() && compressedCode.substring(i, i + subLength).equalsIgnoreCase(subCode)) {
					int asc = list.get(j).asc;
					char asc_code = (char) asc;
					outputText = outputText + asc_code + "";
					i = i + subLength - 1;
					break;
				}
			}
		}
		return outputText;
	}

	public static ArrayList<Nodes> GenerateNode(String[] huffmanCodes) {
		ArrayList<Nodes> list = new ArrayList<Nodes>();
		for (int i = 0; i < huffmanCodes.length; i++) {
			if (huffmanCodes[i] != null) {
				list.add(new Nodes(huffmanCodes[i], i));
			}
		}
		return list;
	}

	public static ArrayList<Nodes> SortList(ArrayList<Nodes> list) {
		for (int i = list.size() - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (list.get(i).code.length() > list.get(j).code.length()) {
					Nodes temp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, temp);
				}
			}
		}
		return list;
	}

}

class Nodes {
	int asc;
	String code;

	public Nodes(String code, int asc) {
		this.asc = asc;
		this.code = code;
	}
}