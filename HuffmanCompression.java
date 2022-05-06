import java.io.*;
import java.util.*;

public class HuffmanCompression {
	public static String getCompressedCode(String inputText, String[] huffmanCodes) {
		String compressedCode = null;
		compressedCode = "";
		char[] ch = inputText.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			int pos = ch[i];
			compressedCode = compressedCode + huffmanCodes[pos];
		}
		return compressedCode;
	}

	public static String[] getHuffmanCode(String inputText) {
		String[] huffmanCodes = new String[128];
		int[] count = new int[128];

		char[] ch = inputText.toCharArray();

		for (int i = 0; i < ch.length; i++) {
			int pos = ch[i];
			count[pos]++;
		}

		ArrayList<Node> NodeList = GenerateNode(count);
		Node root = CreateTree(NodeList);
		HuffCoding(root, "", huffmanCodes);
		return huffmanCodes;
	}

	public static ArrayList<Node> GenerateNode(int[] count) {
		ArrayList<Node> list = new ArrayList<Node>();
		for (int i = 0; i < count.length; i++) {
			if (count[i] > 0) {
				list.add(new Node("", i, count[i]));
			}
		}
		return list;
	}

	public static ArrayList<Node> NodeListSort(ArrayList<Node> NodeList) {
		for (int i = NodeList.size() - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (NodeList.get(i).value > NodeList.get(j).value) {
					Node temp = NodeList.get(i);
					NodeList.set(i, NodeList.get(j));
					NodeList.set(j, temp);
				}
			}
		}
		return NodeList;
	}

	public static Node CreateTree(ArrayList<Node> NodeList) {
		while (NodeList.size() > 1) {
			NodeList = NodeListSort(NodeList);
			Node left = NodeList.get(NodeList.size() - 1);
			Node right = NodeList.get(NodeList.size() - 2);
			Node parent = new Node(null, -1, left.value + right.value);
			parent.left = left;
			parent.right = right;
			NodeList.remove(NodeList.size() - 1);
			NodeList.remove(NodeList.size() - 1);
			NodeList.add(parent);
		}
		return NodeList.get(0);
	}

	public static void HuffCoding(Node node, String huffcode, String[] huffmanCodes) {
		if (node.left != null) {
			HuffCoding(node.left, huffcode + "0", huffmanCodes);
		}
		if (node.right != null) {
			HuffCoding(node.right, huffcode + "1", huffmanCodes);
		}
		if (node.left == null && node.right == null) {
			huffmanCodes[node.asc] = huffcode;
		}
	}

	public static void main(String[] args) throws Exception {
		// obtain input text from a text file encoded with ASCII code
		String inputText = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(args[0])), "US-ASCII");
		// get Huffman codes for each character and write them to a dictionary file
		String[] huffmanCodes = HuffmanCompression.getHuffmanCode(inputText);
		FileWriter fwriter1 = new FileWriter(args[1], false);
		BufferedWriter bwriter1 = new BufferedWriter(fwriter1);
		for (int i = 0; i < huffmanCodes.length; i++)
			if (huffmanCodes[i] != null) {
				bwriter1.write(Integer.toString(i) + ":" + huffmanCodes[i]);
				bwriter1.newLine();
			}
		bwriter1.flush();
		bwriter1.close();
		// get compressed code for input text based on huffman codes of each character
		String compressedCode = HuffmanCompression.getCompressedCode(inputText, huffmanCodes);
		FileWriter fwriter2 = new FileWriter(args[2], false);
		BufferedWriter bwriter2 = new BufferedWriter(fwriter2);
		if (compressedCode != null)
			bwriter2.write(compressedCode);
		bwriter2.flush();
		bwriter2.close();
	}

}

class Node {
	int value;
	int asc;
	String code;
	Node left;
	Node right;

	public Node(String code, int asc, int value) {
		this.value = value;
		this.asc = asc;
		this.code = code;
	}
}
