/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import lglogicpackage.BWinWMixed;
import lglogicpackage.BlackWins;
import lglogicpackage.Board2D;
import lglogicpackage.BomberLogic;
import lglogicpackage.DrawIntercept;
import lglogicpackage.DrawProtect;
import lglogicpackage.FighterLogic;
import lglogicpackage.Gateways;
import lglogicpackage.MixedDraw;
import lglogicpackage.PiecesLogic;
import lglogicpackage.Strategies;
import lglogicpackage.Strategies.MoveStruct;
import lglogicpackage.Strategies.Teams;
import lglogicpackage.TargetLogic;
import lglogicpackage.WhiteWins;
import supportpackage.Coordinates;
import supportpackage.Moves;
import supportpackage.Node;
import supportpackage.Tree;

/**
 *
 * @author nati
 */
public class PanelButtons extends JPanel implements ActionListener, ItemListener{
    
    private ButtonGroup checkBoxes; 
    private final String WHITE = "White Wins";
    private final String BLACK = "Black Wins";
    private final String DRAWI = "Draw Intercept";
    private final String DRAWP = "Draw Protect";
    private final String DRAWM = "Mixed Draw";
    private final String SOL = "Solution";
    private final Board2D startBoard;
    private Board2D currentBoard;
    private Timer timer;
    private JButton next; 
    private JButton chart; 
    private JCheckBox detailI;
    Tree<Moves> currentTree = new Tree<>();
    private Stack <Node<Strategies.MoveStruct>> steptoDraw; 
    private BoardScene scene;
    private Vector3d changeVector = new Vector3d ();
    private PanelTree pTree;
    private String choice = null;
    private Map<Coordinates, String> currentCut = new HashMap <>();
    public JRadioButton blackWins = new JRadioButton();
    public JRadioButton drawIntercept = new JRadioButton();
    public JRadioButton drawProtect = new JRadioButton();
    public JRadioButton mixedDraw = new JRadioButton();
    public JRadioButton whiteWins = new JRadioButton();
    public JRadioButton solution = new JRadioButton();
    private String finalMsg = new String ();
    private GUIFrame frame;
    public static boolean endOfBranch = false;
    
    PanelButtons (BoardScene scene, GUIFrame frame){
        super();
        this.scene = scene;
        this.frame = frame;
        this.checkBoxes = new ButtonGroup ();
        createPanelButtons ();
        this.timer = new Timer(100, this);
        this.startBoard = setStartBoard ();
        this.currentBoard = new Board2D (this.startBoard);
        this.steptoDraw = new Stack<> ();
        this.pTree = new PanelTree ();
        Border empty = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        Border line = BorderFactory.createMatteBorder(0, 10, 10, 10,
                BoardScene.backgroundColor);
        this.setBorder(BorderFactory.createCompoundBorder(line, empty));
        

    }
    
    private Board2D setStartBoard(){
        
        ArrayList <PiecesLogic> pieces = new ArrayList<>();
        pieces.add (new FighterLogic ( "B-Fighter", 0, 5, 2));
        pieces.add (new FighterLogic ("W-Fighter", 7, 7, 1));
        pieces.add (new BomberLogic ("B-Bomber", 7, 4, 2, -1));
        pieces.add(new BomberLogic ("W-Bomber", 2, 5, 1, 1));
        pieces.add( new TargetLogic ("B-Target", 2, 7, 2));
        pieces.add( new TargetLogic ("W-Target", 7, 0, 1));        
        
        return new Board2D(8,8, pieces);
    } 
    
    void createPanelButtons (){
        
        GridLayout layout = new GridLayout(0, 3, 2, 2);
        layout.setHgap(5);
        layout.setVgap(5);
        this.setLayout(layout);
        
        this.whiteWins = new JRadioButton(WHITE);
        this.add(this.whiteWins);
        
        this.blackWins = new JRadioButton (BLACK);
        this.add(blackWins);

        this.detailI = new JCheckBox ("Show Zones");
        this.add (this.detailI);
        
        this.drawIntercept = new JRadioButton(DRAWI);
        this.add(drawIntercept);
        
        this.drawProtect = new JRadioButton (DRAWP);
        this.add(drawProtect);

        this.chart = new JButton ("Space Chart");
        this.add(this.chart);
              
        this.mixedDraw = new JRadioButton (DRAWM);
        this.add(mixedDraw);
        
        this.solution = new JRadioButton (SOL);
        this.add(this.solution);
                   
        this.next = new JButton("Next");
        this.add(this.next);
        
        this.whiteWins.addActionListener(this);
        this.blackWins.addActionListener(this);
        this.drawIntercept.addActionListener(this);
        this.drawProtect.addActionListener(this);
        this.mixedDraw.addActionListener(this);
        this.solution.addActionListener(this);
        this.next.addActionListener(this);
        this.chart.addActionListener(this);
        
        this.detailI.addItemListener(this);
        
        whiteWins.setActionCommand(WHITE);
        this.blackWins.setActionCommand(BLACK);
        this.drawIntercept.setActionCommand(DRAWI);
        this.drawProtect.setActionCommand(DRAWP);
        this.mixedDraw.setActionCommand(DRAWM);
        solution.setActionCommand (SOL);
              
        this.checkBoxes.add(whiteWins);
        this.checkBoxes.add(blackWins);
        this.checkBoxes.add(drawIntercept);
        this.checkBoxes.add(drawProtect);
        this.checkBoxes.add(mixedDraw);
        this.checkBoxes.add(solution);   
    }
    
    final JPanel getPanelButtons (){
        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == this.next)
            try {
                    
                    NextButtonActionPerformed ();
        } catch (IOException ex) {
            Logger.getLogger(PanelButtons.class.getName()).log(Level.SEVERE, null, ex);
        }
        else
        if (e.getSource() == this.chart){
                this.scene.showSpaceChart(this.currentBoard, this,
                        isOnGw(this.currentBoard.getPieceFromName("W-Fighter").getCoordinates(), this.scene.gwProt));
        }        
        else 
            RadioButtonActionPerformed(e);
    }
    
    private void NextButtonActionPerformed () throws IOException{
        
        endOfBranch = false;
        if (PanelTree.currentReason != null)
            CutReason.closeCurrentReason ();
        this.detailI.setSelected(false);
        if(!timer.isRunning()){
            timer.start();
            System.out.println ("start timer");
        }
        boolean willDestroy = false;
        Node<MoveStruct> doMoveNode = null;
        JFrame j = new JFrame();
        if (!this.steptoDraw.isEmpty())
            doMoveNode= this.steptoDraw.pop();
        else{
            endOfBranch = true;
            System.out.println ("No more moves available");
            JOptionPane.showMessageDialog(this.frame, this.finalMsg, "No More Moves",
                     JOptionPane.WARNING_MESSAGE);
            
            return;
        }
        
        Moves doMove = doMoveNode.getData().getMove().getData();
        String namePiece = doMove.getPiece().NAME;
    
        if (!this.currentBoard.equals(doMoveNode.getData().getBoard())){
            
            this.currentBoard = new Board2D (doMoveNode.getData().getBoard());
            this.scene.calculateGateways (this.currentBoard);

            SetGwInBoard (this.scene.gwInt, this.scene.gwPiecesInt, Gateways.Types.INTERCEPT);
            SetGwInBoard (this.scene.gwProt, this.scene.gwPiecesPro, Gateways.Types.PROTECT);
     
            setPiecesToCurrent ("nothing");
            this.steptoDraw.add (doMoveNode);
            if (!this.blackWins.isSelected())
                this.pTree.changeBranch();
            return;
        }
        BoardObjects destroyed = null;
        String destroyedName = null;
        willDestroy = this.currentBoard.hasPiece(doMove.getStep());
        Vector3d locDestroyed = new Vector3d ();
        
        if (willDestroy){
            destroyedName =this.currentBoard.getPiece(doMove.getStep()).NAME;
            destroyed = selectBoardObj (destroyedName);
            destroyed.t3d.get(locDestroyed);
        }
        
        this.pTree.callRepaint (doMoveNode.getData().getMove());
        PiecesLogic piece = this.currentBoard.getPieceFromName(doMove.getPiece().NAME);
        
        int difx = doMove.getStep().x - piece.positionX ;
        int dify = doMove.getStep().y - piece.positionY ;
        
        Vector3d loc = new Vector3d();
 
        doMoveNode.getData().getBoard().makeMove(doMove);
        this.currentBoard = new Board2D (doMoveNode.getData().getBoard());
        addChildrenToBoardStack (doMoveNode);
        
        BoardObjects boardObj = selectBoardObj (namePiece);
        Vector3d stop = Coordinates.convertToGraph(new Vector3d (doMove.getStep().x, boardObj.translateVec.y, 
                doMove.getStep().y));
        boardObj.t3d.get(loc);        
        this.changeVector = new Vector3d ();         
        
        while (stopCondition (loc, stop, difx, dify)){  
            
            boardObj.t3d.setTranslation(this.changeVector);
            boardObj.tg.setTransform(boardObj.t3d);
            boardObj.t3d.get(loc);  
            
            if (namePiece.equals ("B-Fighter")){
                this.scene.calculateGateways (this.currentBoard);
                TransformGw (this.scene.gwProt, this.scene.gwPiecesPro, Gateways.Types.PROTECT);
            }        
        
            if (namePiece.equals ("B-Bomber")){
                this.scene.calculateGateways (this.currentBoard);
                TransformGw (this.scene.gwInt, this.scene.gwPiecesInt, Gateways.Types.INTERCEPT);
            }  
   
        } 

        if (namePiece.equals ("W-Fighter") || namePiece.equals ("B-Bomber")){
            this.scene.calculateGateways(this.currentBoard);
            SetGwInBoard(this.scene.gwInt, this.scene.gwPiecesInt, Gateways.Types.INTERCEPT);      
        }
         if (namePiece.equals ("W-Fighter") || namePiece.equals ("B-Fighter")) {
            //this.scene.calculateGateways(this.currentBoard); 
            SetGwInBoard(this.scene.gwProt, this.scene.gwPiecesPro, Gateways.Types.PROTECT); 
            //removeAddNoTransformsGwPositions(this.scene.gwProt, this.scene.gwPiecesPro, Gateways.Types.PROTECT);
         }
        if (willDestroy)
            setPiecesToCurrent (destroyedName); 
       //else 
         //   setPiecesToCurrent ("nothing");
           
    }
   
    public static boolean isOnGw (Coordinates fighter, ArrayList <Coordinates> gw){
        
        for (Coordinates c: gw)
            if (c.equals(fighter))
                return true;
            
         return false;   

    }
    
    private void SetGwInBoard (ArrayList<Coordinates> to, BoardObjects[] from, Gateways.Types type){
       
        Coordinates fCoor = this.currentBoard.getPieceFromName("W-Fighter").getCoordinates();
        if (Gateways.Types.PROTECT.equals(type) && isOnGw(fCoor, this.scene.gwProt)){
            to= new ArrayList <>();
            to.add (fCoor);
        }    
        else{
            if (Gateways.Types.INTERCEPT.equals(type) && isOnGw(fCoor, this.scene.gwInt)){
            to= new ArrayList <>();
            to.add (fCoor);
            }
        }          
        
        Vector3d locGw = new Vector3d();
        int index;
        int [] track = new int[]{99,99,99,99,99};

        for (Coordinates c: to){
            if (Gateways.Types.PROTECT.equals(type)){
                index = c.y;
            }    
            else{
                index = c.x;
            }    
            if (from[index] != null ){
                from[index].t3d.get(locGw);
                from[index].t3d.setTranslation(Coordinates.convertToGraph(new Vector3d (c.x, locGw.y,c.y)));
                from[index].tg.setTransform(from[index].t3d);
                track[index-3] = 1;
                
            }               
            else{
                throw new IllegalArgumentException ("gateways need default");
            }    
        }
        
        for (int i = 0; i < track.length; i++)
            if (track[i] == 99)
               setGwToDefault (from[i+3]);
    }
    
    private void removeAddNoTransformsGwPositions (ArrayList<Coordinates> to, BoardObjects[] check, Gateways.Types type){
        
        int coor;
        for (int i=3; i< check.length; i++){
            for (Coordinates c: to){
                if (Gateways.Types.PROTECT.equals(type))
                    coor = c.y; 
                else
                    coor = c.x;
                if (coor == i){
                    if (!check[i].translateVec.equals(Coordinates.convertToGraph(new Vector3d (c.x, check[i].translateVec.y, c.y))))
                        check[i].t3d.setTranslation(Coordinates.convertToGraph(new Vector3d (c.x, check[i].translateVec.y,c.y)));
                        check[i].tg.setTransform(check[i].t3d);
                }
            }   
        }    
    }
    
    private void TransformGw (ArrayList<Coordinates> to, BoardObjects[] from, Gateways.Types type){
        
        Vector3d locGw = new Vector3d();
        float changex, changez;
        int index;
        int [] track = new int[]{99,99,99,99,99};

        for (Coordinates c: to){
            if (Gateways.Types.PROTECT.equals(type)){
                index = c.y;
                changex= .1f;
                changez = 0;
            }    
            else{
                index = c.x;
                changex= 0;
                changez = -.1f;
            }    
            if (from[index] != null ){
                from[index].t3d.get(locGw);
                from[index].t3d.setTranslation(new Vector3d (locGw.x+changex,
                        locGw.y, locGw.z+changez));
                from[index].tg.setTransform(from[index].t3d);
                track[index-3] = 1;
                
            }               
            else{
                throw new IllegalArgumentException ("gateways need default");
            }    
        }
        
        for (int i = 0; i < track.length; i++)
            if (track[i] == 99)
               setGwToDefault (from[i+3]);
  
    }
    
    private void setGwToDefault (BoardObjects piece){
        piece.t3d.setTranslation(Coordinates.convertToGraph(BoardScene.defaultV));
        piece.tg.setTransform(piece.t3d);
    }
    
    
    private void RadioButtonActionPerformed(ActionEvent evt){

        if (evt.getActionCommand()== null)
           return; 
        this.detailI.setSelected(false);
        pTree.setRadioPressed(true);
        pTree.repaint();
        this.choice = evt.getActionCommand();
        switch (this.choice){
            case WHITE:
                this.currentTree = strategyTree ("White wins"); 
                setPiecesToCurrent ("nothing");
            break;    
            case BLACK:
                this.currentTree = strategyTree ("Black wins");
                setPiecesToCurrent ("nothing");
            break;
            case DRAWI:
                this.currentTree = strategyTree ("Draw Int");
                setPiecesToCurrent ("nothing");
            break;   
            case DRAWP:
                this.currentTree = strategyTree ("Draw Pro");
                setPiecesToCurrent ("nothing");
            break; 
            case DRAWM:
                this.currentTree = strategyTree ("Mixed Draw");
                setPiecesToCurrent ("nothing");
            break;                 
            default:
                this.currentTree = strategyTree ("Solution");
                setPiecesToCurrent ("nothing");
            break;    
        }
        this.choice = null;
        setBoardStartStack();
        this.pTree.setTreeStartStack(this.currentTree.getRoot(), this.currentCut);
        this.scene.calculateGateways (this.currentBoard);
        SetGwInBoard (this.scene.gwInt, this.scene.gwPiecesInt, Gateways.Types.INTERCEPT);
        SetGwInBoard (this.scene.gwProt, this.scene.gwPiecesPro, Gateways.Types.PROTECT);
    }
    
    private Tree<Moves> strategyTree (String s){
        this.currentBoard = new Board2D (setStartBoard());
        Strategies.restartSd ();
        
        switch (s){
            case "Solution":
                BWinWMixed strategy = new BWinWMixed (this.currentBoard, Teams.WHITE); 
                strategy.createTree();
                this.currentCut = strategy.getCutReasons();
                this.finalMsg = new String ("Terminal State of All Branches: DRAW");
                return strategy.getTree();
            case "Black wins":
                BlackWins bTest = new BlackWins(this.currentBoard);
                bTest.evaluateBlackWins();
                this.currentCut = bTest.getCutReasons();
                this.finalMsg = new String ("B-Win CANNOT be eliminated.");
                return bTest.getTree();
            case "Draw Int":   
                DrawIntercept dTest = new DrawIntercept (this.currentBoard, Teams.WHITE); 
                dTest.evaluateDrawIntercept();
                this.currentCut = dTest.getCutReasons();
                dTest.getTree().printTreeRelationsMoves();
                this.finalMsg = new String ("Eliminate Pure Draw Intercept");
                return dTest.getTree();
            case "Draw Pro":
                DrawProtect pTest = new DrawProtect (this.currentBoard, Teams.WHITE); 
                pTest.evaluateDrawProtect();
                this.currentCut = pTest.getCutReasons();
                this.finalMsg = new String ("Eliminate Pure Draw Protect");
                return pTest.getTree();         
            case "Mixed Draw":
                MixedDraw mTest = new MixedDraw (this.currentBoard, Teams.WHITE); 
                mTest.evaluateMixedDraw();
                this.currentCut = mTest.getCutReasons();
                this.finalMsg = new String ("Mixed Draw CANNOT be eliminated");
                return mTest.getTree();
            default:  
            case "White wins": 
                WhiteWins test = new WhiteWins (this.currentBoard);
                test.evaluateWhiteWins();
                this.currentCut = test.getCutReasons();
                this.finalMsg = new String ("Eliminate W-Win");
                return test.getTree();
        }
 
    }
    
    private boolean stopCondition (Vector3d loc, Vector3d stop, int difx, int difz){
        if (difx == 0 && difz == 1){
            this.changeVector = new Vector3d (loc.x, loc.y , loc.z+.1f );
            return (stop.z > loc.z );
        }            ;
        if (difx == 0 && difz == -1){
            this.changeVector = new Vector3d (loc.x, loc.y , loc.z-.1f );
            return (stop.z< loc.z);
        }    
        if (difx == 1 && difz == 0){
            this.changeVector = new Vector3d (loc.x-.1f, loc.y , loc.z );
            return (stop.x < loc.x );
        }    
        if (difx == -1 && difz == 0){
            this.changeVector = new Vector3d (loc.x+.1f, loc.y , loc.z );
            return (stop.x> loc.x);
        }    
        if (difx == 1 && difz == 1){
            this.changeVector = new Vector3d (loc.x-.1f, loc.y , loc.z +.1f);
            return (stop.x < loc.x )&&(stop.z > loc.z );
        }    
        if (difx == -1 && difz == -1){
            this.changeVector = new Vector3d (loc.x+.1f, loc.y , loc.z -.1f);
            return (stop.x > loc.x )&&(stop.z < loc.z );
        }    
        if (difx == -1 && difz == 1){
            this.changeVector = new Vector3d (loc.x+.1f, loc.y , loc.z +.1f);
            return (stop.x > loc.x )&&(stop.z > loc.z );
        }    
        if (difx == 1 && difz == -1){
            this.changeVector = new Vector3d (loc.x-.1f, loc.y , loc.z -.1f);
            return (stop.x < loc.x )&&(stop.z < loc.z );
        }    
        
        throw new IllegalArgumentException ("case not considered in difference ");
    }
    
    private void setPiecesToCurrent (String doNotSet){
        Vector3d loc = new Vector3d();
        PiecesLogic piece;
        
        if( !doNotSet.equals ("W-Fighter"))
            piece = this.currentBoard.getPieceFromName("W-Fighter");     
        else
            piece = new BomberLogic (null, -1,8,0,0);
        setPiecesToCurrentHelper (this.scene.whiteFighter, piece);

        if( !doNotSet.equals ("B-Fighter"))
            piece = this.currentBoard.getPieceFromName("B-Fighter");    
        else
            piece = new BomberLogic (null, -1,8,0,0);
        setPiecesToCurrentHelper (this.scene.blackFighter, piece);
        
        if ( !doNotSet.equals ("W-Bomber"))
            piece = this.currentBoard.getPieceFromName("W-Bomber");  
        else
            piece = new BomberLogic (null, -1,8,0,0);
        setPiecesToCurrentHelper (this.scene.whiteBomber, piece);
     
        if (!doNotSet.equals ("B-Bomber"))
            piece = this.currentBoard.getPieceFromName("B-Bomber");  
        else    
            piece = new BomberLogic (null, -1,8,0,0);
        setPiecesToCurrentHelper (this.scene.blackBomber, piece);
        
    }
    
    private void setPiecesToCurrentHelper (BoardObjects boardObj, PiecesLogic piece){
            Vector3d loc = Coordinates.convertToGraph (new Vector3d (piece.positionX, 
                    boardObj.translateVec.y,piece.positionY));
            boardObj.t3d.setTranslation(loc);
            boardObj.tg.setTransform(boardObj.t3d);
    }
    
    private BoardObjects selectBoardObj (String Name ){
        switch (Name){
            case ("W-Bomber"):
                return this.scene.whiteBomber;
            case ("B-Bomber"):
                return this.scene.blackBomber;
            case ("W-Fighter"):
                return this.scene.whiteFighter ;
            default:
                return this.scene.blackFighter;
        }
    }
    
    private void setBoardStartStack (){
        PanelTree.repaintCallsCounter = 0;
        this.steptoDraw = new Stack <> ();
        addChildrenToBoardStack (new Node (new MoveStruct (this.currentTree.getRoot(), this.startBoard)));
        
    }
    
    private void addChildrenToBoardStack (Node<MoveStruct> dad){
        List <Node <Moves>>  rootChildren = dad.getData().getMove().getChildren();
        Node<MoveStruct> newM;
        if (!rootChildren.isEmpty())
            for (Node<Moves> child : rootChildren){
                newM = new Node (new MoveStruct (child,new Board2D (dad.getData().getBoard())));
                newM.setFather(dad);
                this.steptoDraw.add (newM);
            }        
    }
    
    public JPanel getPanelTree(){
        return this.pTree;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (source == this.detailI)
            DetailIStateChanged (e);   
    }

    private void DetailIStateChanged (ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED){
            if (this.drawIntercept.isSelected() ||this.drawProtect.isSelected()
                    || this.mixedDraw.isSelected())
                this.scene.showSdTrajectories(this.currentBoard);
            else 
                this.scene.showZones(this.currentBoard);
        }    
        else 
            this.scene.removeZones();
    }    

    
}
