package dev.ahnaf30eidiot.tok.render;

import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class TOKSizedResourceTexture extends ResourceTexture {

    private int width = 1;
    private int height = 1;

    public TOKSizedResourceTexture(Identifier location) {
        super(location);
    }

    @Override
    public void load(ResourceManager manager) throws java.io.IOException {
        super.load(manager);
        try {
            var texData = this.loadTextureData(manager);
            var image = texData.getImage();
            if (image != null) {
                this.width = image.getWidth();
                this.height = image.getHeight();
            }
        } catch (Exception ignored) {
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
