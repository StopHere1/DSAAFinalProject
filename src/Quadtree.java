import java.util.ArrayList;

public class Quadtree {

    private int MAX_OBJECTS = 4;
    private int MAX_LEVELS = 5;

    private int level;        // 子节点深度
    private ArrayList<Rectangle> objects;     // 物体数组
    private Rectangle bounds; // 区域边界
    private Quadtree[] nodes; // 四个子节点
    private ArrayList<Integer> particlenumber;

    /*
     *
     */
    public Quadtree(int pLevel, Rectangle pBounds) {
        level = pLevel;
        objects = new ArrayList();
        particlenumber=new ArrayList();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    public void clear() {
        objects.clear();
        particlenumber.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        int subWidth = (int) (bounds.getWidth() / 2);
        int subHeight = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }
    /*
     * 用于判断物体属于哪个子节点
     * -1指的是当前节点可能在子节点之间的边界上不属于四个子节点而还是属于父节点
     */

    private int getIndex(Rectangle pRect) {
        int index = -1;
        // 中线
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // 物体完全位于上面两个节点所在区域
        boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
        // 物体完全位于下面两个节点所在区域
        boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

        // 物体完全位于左面两个节点所在区域
        if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1; // 处于左上节点
            } else if (bottomQuadrant) {
                index = 2; // 处于左下节点
            }
        }
        // 物体完全位于右面两个节点所在区域
        else if (pRect.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0; // 处于右上节点
            } else if (bottomQuadrant) {
                index = 3; // 处于右下节点
            }
        }

        return index;
    }

    /*
     * 将物体插入四叉树
     * 如果当前节点的物体个数超出容量了就将该节点分裂成四个从而让多数节点分给子节点
     */
    public void  particleinsert(Particle particle,double dt,int i,double n){
        Rectangle pRect=transform(particle,dt,n);
        insert(pRect,i);
    }



    public void insert(Rectangle pRect,int number) {
        // 插入到子节点
        if (nodes[0] != null) {
            int index = getIndex(pRect);

            if (index != -1) {
                nodes[index].insert(pRect,number);
                return;
            }
        }

        // 还没分裂或者插入到子节点失败，只好留给父节点了
        objects.add(pRect);
        particlenumber.add(number);

        // 超容量后如果没有分裂则分裂
        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }
            // 分裂后要将父节点的物体分给子节点们
            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.remove(i),particlenumber.remove(i));
                }
                else {
                    i++;
                }
            }
        }
    }


    private Rectangle transform(Particle circle, double dt,double axsis) {
        double dtx = circle.timeToHitVerticalWall();
        double dty = circle.timeToHitHorizontalWall();
        double dt0=0;
        if (dtx >= dt && dty >= dt) {
            dt0 = dt;
        } else if (dtx < dty && dtx < dt) {
            dt0 = dtx;
        } else if (dtx >= dty && dty < dt){
            dt0 = dty;
        }
        double x = circle.getRx()-circle.getRadius()+axsis;
        double y = circle.getRy() - circle.getRadius()+axsis;
        double height = circle.getVy() * dt0;
        double width = circle.getVx() * dt0;
        if (height<0){
            y= circle.getRy()+height-circle.getRadius()+axsis;
            height=-height;
        }
        if (width<0){
            x=circle.getRx()+width-circle.getRadius()+axsis;
            width=-width;
        }
        width=width+2*circle.getRadius();
        height=height+2*circle.getRadius();

        Rectangle trans =new Rectangle(x,y,width,height);
        return trans;
    }
    /*
     * 返回所有可能和指定物体碰撞的物体
     *
     *
     */
    public ArrayList retrieve(Particle particle,double dt,double axsis){
        Rectangle pRect=transform(particle,dt,axsis);
        ArrayList returnObjects=new ArrayList();
         return  retrieve(returnObjects,pRect);

    }

    public ArrayList retrieve(ArrayList returnObjects, Rectangle pRect) {
        int index = getIndex(pRect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, pRect);
        }
        else if(index==-1&&nodes[0]!=null){
            nodes[0].retrieve(returnObjects, pRect);
            nodes[1].retrieve(returnObjects,pRect);
            nodes[2].retrieve(returnObjects,pRect);
            nodes[3].retrieve(returnObjects,pRect);
        }

        returnObjects.addAll(particlenumber);

        return returnObjects;
    }


}