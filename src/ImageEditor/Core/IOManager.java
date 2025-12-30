package ImageEditor.Core;

import ImageEditor.IllegalFormatException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOManager {
    private final File file;
    private final String fileFormat;



    public IOManager(String filepath) throws IllegalFormatException {
        this.file = new File(filepath);

        int dotIndex = filepath.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filepath.length() - 1)
            throw new IllegalFormatException("Illegal file format");
        else{
            this.fileFormat = filepath.substring(dotIndex + 1);
        }
    }


    public BufferedImage getImage() throws IOException {
        return ImageIO.read(file);
    }

    public void exportImage(BufferedImage image) throws IOException {
        ImageIO.write(image, this.fileFormat, file);
    }

    public void exportImage(BufferedImage image, String format, String path) throws IOException {
        ImageIO.write(image, format, new File(path));
    }
}
