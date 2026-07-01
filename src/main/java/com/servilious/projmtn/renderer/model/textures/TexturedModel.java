package com.servilious.projmtn.renderer.model.textures;

import com.servilious.projmtn.renderer.model.BaseModel;
import java.util.Objects;

public class TexturedModel {
    private BaseModel model;
    private ModelTexture tex;

    public TexturedModel(BaseModel model, ModelTexture tex) {
       this.model = model;
       this.tex = tex;
    }

    public BaseModel getModel() {
        return this.model;
    }

    public ModelTexture getModelTex() {
        return this.tex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TexturedModel that = (TexturedModel) o;
        return Objects.equals(model, that.model) && Objects.equals(tex, that.tex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, tex);
    }
}
