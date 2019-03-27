package three.renderers.gl;

import java.util.ArrayList;
import java.util.Collections;

import three.core.BufferGeometry;
import three.core.Object3D;
import three.materials.Material;
import three.util.GeoMatGroup;
import three.util.PainterSortStable;
import three.util.RenderItem;
import three.util.ReversePainterSortStable;

public class GLRenderList {
    public ArrayList<RenderItem> renderItems;
    public int renderItemsIndex;
    public ArrayList<RenderItem> opaque;
    public ArrayList<RenderItem> transparent;

    public GLRenderList(){
        renderItems = new ArrayList<RenderItem>();
        this.renderItemsIndex = 0;
        opaque = new ArrayList<>();
        transparent = new ArrayList<>();
    }

    public void init(){
        renderItemsIndex = 0;
        opaque.clear();
        transparent.clear();
    }

    private RenderItem getNextRenderItem(Object3D object, BufferGeometry geometry, Material material, int z, GeoMatGroup group){
        RenderItem renderItem = null;
        if(renderItems.size() > renderItemsIndex){
            renderItem = renderItems.get(renderItemsIndex);
        }

        if ( renderItem == null ) {
            renderItem = new RenderItem();
            renderItem.id = object.id;
            renderItem.object = object;
            renderItem.geometry = geometry;
            renderItem.material = material;
            renderItem.program = material.program;
            renderItem.renderOrder = object.renderOrder;
            renderItem.z = z;
            renderItem.group = group;

            renderItems.add(renderItemsIndex, renderItem);

        } else {
            renderItem.id = object.id;
            renderItem.object = object;
            renderItem.geometry = geometry;
            renderItem.material = material;
            renderItem.program = material.program;
            renderItem.renderOrder = object.renderOrder;
            renderItem.z = z;
            renderItem.group = group;
        }

        renderItemsIndex ++;

        return renderItem;
    }

    public void push(Object3D object, BufferGeometry geometry, Material material, int z, GeoMatGroup group){
        RenderItem renderItem = getNextRenderItem( object, geometry, material, z, group );
        if(material.transparent){
            transparent.add(renderItem);
        }else{
            opaque.add(renderItem);
        }
    }

    public void unshift(Object3D object, BufferGeometry geometry, Material material, int z, GeoMatGroup group){
        RenderItem renderItem = getNextRenderItem( object, geometry, material, z, group );

        if(material.transparent){
            transparent.add(0, renderItem);
        }else{
            opaque.add(0, renderItem);
        }

    }

    public void sort(){
        if(opaque.size() > 1){
            Collections.sort(opaque, new PainterSortStable());
        }
        if(transparent.size() > 1){
            Collections.sort(opaque, new ReversePainterSortStable());
        }
    }

}
