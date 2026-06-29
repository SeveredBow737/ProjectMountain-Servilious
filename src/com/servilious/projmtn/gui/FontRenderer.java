package com.servilious.projmtn.gui;

import com.servilious.projmtn.renderer.VertexLoader;
import com.servilious.projmtn.renderer.model.BaseModel;
import com.servilious.projmtn.util.MathHelper;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class FontRenderer {
    private final BaseModel quad;
    private FontShader shader;


    public FontRenderer(VertexLoader loader) {
        float[] positions = {
                -1, 1,
                -1, -1,
                1, 1,
                1, -1
        };
        float[] texCoords = {
                0 + 15, 1 + 15,
                0+ 15, 0 + 15,
                1+ 15, 1 + 15,
                1+ 15, 0+ 15
        };
        quad = loader.loadToVAO(positions, texCoords, 2);
        shader = new FontShader();
    }



    public void renderFont(GuiTexture font) {
        shader.start();
        glBindVertexArray(quad.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, font.getTex());
        Matrix4f mat = MathHelper.setupTransformationMatrix(font.getPos(), font.getScale());
        shader.loadTransformation(mat);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.stop();
    }

    public void clear() {
        shader.clear();
    }
}
