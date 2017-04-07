package de.tudresden.inf.mms;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * JFrame-Anwendung (Fenster mit Bildansicht und Funktionsauswahl) <br>
 * <strong>Nicht bearbeiten!</strong>
 */
public class ImageViewer extends JFrame {

	private static final long serialVersionUID = 1L;
	ImageIcon myImage;
	File file = new File("bild.jpg");
	ImageEditor myEditor;

	JPanel imagePanel = new JPanel();

	public ImageViewer() {
		try {
			myImage = new ImageIcon(file.getAbsolutePath());
			myEditor = new ImageEditor(ImageIO.read(file));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		imagePanel.add(new JLabel(myImage));
		imagePanel.setBackground(Color.WHITE);

		add(imagePanel, BorderLayout.CENTER);

		final JComboBox<String> operator = new JComboBox<String>();
		operator.addItem("Graustufen");
		operator.addItem("Invertieren");
		operator.addItem("Rotation");
		operator.addItem("Histogrammausgleich");
		operator.addItem("Unterabtastung");
		operator.addItem("JPEG-Farbunterabtastung");
		operator.addItem("Farbquantisierung");
		operator.addItem("Ordered-Dithering");
		operator.addItem("Floyd-Steinberg-Dithering");

		JPanel Menu = new JPanel();
		Menu.setBackground(Color.WHITE);
		add(Menu, BorderLayout.NORTH);

		Menu.add(operator);

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Image img = myImage.getImage();
				switch (operator.getSelectedItem().toString()) {
				case "Graustufen":
					img = myEditor.grayscale();
					break;
				case "Invertieren":
					img = myEditor.invert();
					break;
				case "Rotation":
					img = myEditor.rotate();
					break;
				case "Histogrammausgleich":
					img = myEditor.linHist();
					break;
				case "Unterabtastung":
					img = myEditor.subsampling();
					break;
				case "JPEG-Farbunterabtastung":
					img = myEditor.colorSubsampling();
					break;
				case "Farbquantisierung":
					img = myEditor.colorQuant();
					break;
				case "Ordered-Dithering":
					img = myEditor.orderedDithering();
					break;
				case "Floyd-Steinberg-Dithering":
					img = myEditor.floydSteinbergDithering();
					break;
				}
				myImage.setImage(img);
				imagePanel.validate();
				imagePanel.repaint();
			}
		});

		Menu.add(ok);

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ImageViewer frame = new ImageViewer();

		frame.setSize(frame.myImage.getIconWidth() + 50,
				frame.myImage.getIconHeight() + 100);
		frame.setTitle("Aufgabe 1");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}

}
