package ImageEditor.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IOManager {
    private final File file;
    private final String fileFormat;



    public IOManager(String filepath) throws IOException {
        this.file = new File(filepath);

        int dotIndex = filepath.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filepath.length() - 1)
            throw new IOException("Illegal file format");
        else{
            this.fileFormat = filepath.substring(dotIndex + 1);
        }
    }


    public BufferedImage getImage() throws IOException {
        return ImageIO.read(file);
    }

    public void exportImage(BufferedImage image) throws IOException {
        File output = new File("newImage." + this.fileFormat);
        ImageIO.write(image, this.fileFormat, output);
    }

    public void exportImage(BufferedImage image, String format, String path) throws IOException {
        ImageIO.write(image, format, new File(path));
    }
}
