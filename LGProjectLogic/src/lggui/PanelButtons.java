/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lggui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import javax.vecmath.Vector3d;
import lglogicpackage.BWinWMixed;
import lglogicpackage.BlackWins;
import lglogicpackage.Board2D;
import lglogicpackage.BomberLogic;
import lglogicpackage.DrawIntercept;
import lglogicpackage.DrawProtect;
import lglogicpackage.FighterLogic;
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
public class PanelButtons extends JPanel implements ActionListener{
    
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
    Tree<Moves> currentTree = new Tree<>();
    private Stack <Node<Strategies.MoveStruct>> steptoDraw; 
    private BoardScene scene;
    private Vector3d changeVector = new Vector3d ();
    private PanelTree pTree;
    
    PanelButtons (BoardScene scene){
        super();
        this.scene = scene;
        this.checkBoxes = new ButtonGroup ();
        createPanelButtons ();
        this.timer = new Timer(100, this);
        this.startBoard = setStartBoard ();
        this.currentBoard = new Board2D (this.startBoard);
        this.steptoDraw = new Stack<> ();
        this.pTree = new PanelTree ();
        

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
        this.setLayout(layout);
        
        JRadioButton whiteWins = new JRadioButton(WHITE);
        this.add(whiteWins);
        
        JRadioButton blackWins = new JRadioButton (BLACK);
        this.add(blackWins);
        
        this.next = new JButton("Next");
        this.add(this.next);
        
        JRadioButton drawIntercept = new JRadioButton(DRAWI);
        this.add(drawIntercept);
        
        JRadioButton drawProtect = new JRadioButton (DRAWP);
        this.add(drawProtect);
        
        this.add (new JPanel());
        
        JRadioButton mixedDraw = new JRadioButton (DRAWM);
        this.add(mixedDraw);
        
        JRadioButton solution = new JRadioButton (SOL);
        this.add(solution);
        
        whiteWins.addActionListener(this);
        blackWins.addActionListener(this);
        drawIntercept.addActionListener(this);
        drawProtect.addActionListener(this);
        mixedDraw.addActionListener(this);
        solution.addActionListener(this);
        this.next.addActionListener(this);
        
        whiteWins.setActionCommand(WHITE);
        blackWins.setActionCommand(BLACK);
        drawIntercept.setActionCommand(DRAWI);
        drawProtect.setActionCommand(DRAWP);
        mixedDraw.setActionCommand(DRAWM);
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
        else {

            RadioButtonActionPerformed(e);
        }    
    }
    
    private void NextButtonActionPerformed () throws IOException{
    
        if(!timer.isRunning()){
            timer.start();
            System.out.println ("start timer");
        }
        
        Node<MoveStruct> doMoveNode = null;
        if (!this.steptoDraw.isEmpty())
            doMoveNode= this.steptoDraw.pop();
        else{
            System.out.println ("No more moves available");
            return;
        }
        
        Moves doMove = doMoveNode.getData().getMove().getData();
        if (!this.currentBoard.equals(doMoveNode.getData().getBoard())){
            this.currentBoard = new Board2D (doMoveNode.getData().getBoard());
            setPiecesToCurrent ();
            this.steptoDraw.add (doMoveNode);
            return;
        }
        this.pTree.callRepaint (doMoveNode.getData().getMove());
        PiecesLogic piece = this.currentBoard.getPieceFromName(doMove.getPiece().NAME);
        
        int difx = doMove.getStep().x - piece.positionX ;
        int dify = doMove.getStep().y - piece.positionY ;
        
        Vector3d loc = new Vector3d();
        Vector3d stop = Coordinates.convertToGraph(new Vector3d (doMove.getStep().x, 0, 
                doMove.getStep().y));
        
        doMoveNode.getData().getBoard().makeMove(doMove);
        this.currentBoard = new Board2D (doMoveNode.getData().getBoard());
        addChildrenToBoardStack (doMoveNode);

        
        
        BoardObjects boardObj = selectBoardObj (doMove.getPiece().NAME);
        
        boardObj.t3d.get(loc);        
        this.changeVector = new Vector3d (); 
        
        while (stopCondition (loc, stop, difx, dify)){  
            
            boardObj.t3d.setTranslation(this.changeVector);
            boardObj.tg.setTransform(boardObj.t3d);
            boardObj.t3d.get(loc);  
        } 
           
    }

    private void RadioButtonActionPerformed(ActionEvent evt){

      
        if (evt.getActionCommand()== null)
           return; 
        pTree.setRadioPressed(true);
        pTree.repaint();
        String choice = evt.getActionCommand();
        switch (choice){
            case WHITE:
                this.currentTree = strategyTree ("White wins"); 
                setPiecesToCurrent ();
            break;    
            case BLACK:
                this.currentTree = strategyTree ("Black wins");
                setPiecesToCurrent ();
            break;
            case DRAWI:
                this.currentTree = strategyTree ("Draw Int");
                setPiecesToCurrent ();
            break;   
            case DRAWP:
                this.currentTree = strategyTree ("Draw Pro");
                setPiecesToCurrent ();
            break; 
            case DRAWM:
                this.currentTree = strategyTree ("Mixed Draw");
                setPiecesToCurrent ();
            break;                 
            default:
                this.currentTree = strategyTree ("Solution");
                setPiecesToCurrent ();
            break;    
        }
        
        setBoardStartStack();
        this.pTree.setTreeStartStack(this.currentTree.getRoot());
    }
    
    private Tree<Moves> strategyTree (String s){
        this.currentBoard = new Board2D (setStartBoard());
        Strategies.restartSd ();
        
        switch (s){
            case "Solution":
                BWinWMixed strategy = new BWinWMixed (this.currentBoard, Teams.WHITE); 
                strategy.createTree();
                return strategy.getTree();
            case "Black wins":
                BlackWins bTest = new BlackWins(this.currentBoard);
                bTest.evaluateBlackWins();
                return bTest.getTree();
            case "Draw Int":   
                DrawIntercept dTest = new DrawIntercept (this.currentBoard, Teams.WHITE); 
                dTest.evaluateDrawIntercept();
                dTest.getTree().printTreeRelationsMoves();
                return dTest.getTree();
            case "Draw Prot":
                DrawProtect pTest = new DrawProtect (this.currentBoard, Teams.WHITE); 
                pTest.evaluateDrawProtect();
                return pTest.getTree();         
            case "Mixed Draw":
                MixedDraw mTest = new MixedDraw (this.currentBoard, Teams.WHITE); 
                mTest.evaluateMixedDraw();
                mTest.getTree();
            default:  
            case "White wins": 
                WhiteWins test = new WhiteWins (this.currentBoard);
                test.evaluateWhiteWins();
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
    
    private void setPiecesToCurrent (){
        Vector3d loc = new Vector3d();
        PiecesLogic piece;
        piece = this.currentBoard.getPieceFromName("W-Fighter");        
        setPiecesToCurrentHelper (this.scene.whiteFighter, piece);
        
        piece = this.currentBoard.getPieceFromName("B-Fighter");        
        setPiecesToCurrentHelper (this.scene.blackFighter, piece);
        
        piece = this.currentBoard.getPieceFromName("W-Bomber");        
        setPiecesToCurrentHelper (this.scene.whiteBomber, piece);
        
        piece = this.currentBoard.getPieceFromName("B-Bomber");        
        setPiecesToCurrentHelper (this.scene.blackBomber, piece);
    }
    
    private void setPiecesToCurrentHelper (BoardObjects boardObj, PiecesLogic piece){
            Vector3d loc = Coordinates.convertToGraph (new Vector3d (piece.positionX, 
                    BoardScene.y,piece.positionY));
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


}
