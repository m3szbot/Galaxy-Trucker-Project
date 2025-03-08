<mxfile host="app.diagrams.net" agent="Mozilla/5.0 (X11; Linux x86_64; rv:136.0) Gecko/20100101 Firefox/136.0" version="26.1.0">
  <diagram name="Page-1" id="LL5o3QV-uiALjf6wWWii">
    <mxGraphModel dx="16221" dy="4658" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="827" pageHeight="1169" math="0" shadow="0">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        <mxCell id="Ru8jyAiUZzCANUr1jurh-29" value="" style="shape=folder;fontStyle=1;tabWidth=80;tabHeight=30;tabPosition=left;html=1;boundedLbl=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-6340" y="2630" width="980" height="600" as="geometry" />
        </mxCell>
        <mxCell id="HjN6oJ3qzjS9kNq-yB3L-1" value="" style="shape=folder;fontStyle=1;spacingTop=10;tabWidth=40;tabHeight=14;tabPosition=left;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-9190" y="560" width="680" height="410" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-150" value="" style="shape=folder;fontStyle=1;spacingTop=10;tabWidth=40;tabHeight=14;tabPosition=left;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-9940" y="1290" width="3220" height="1800" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-91" value="" style="group" parent="1" connectable="0" vertex="1">
          <mxGeometry x="-9198" y="3240" width="1030" height="490" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-88" value="" style="shape=folder;fontStyle=1;spacingTop=10;tabWidth=120;tabHeight=30;tabPosition=left;html=1;whiteSpace=wrap;" parent="I8JL-FB-3HwPmqZuM6Aw-91" vertex="1">
          <mxGeometry x="30" y="50" width="830" height="420" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-1" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;Deck&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- numCards: int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- inUse: bool&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- cardList: List&amp;lt;carta&amp;gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ Deck()&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getNumCards(): int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getInUse(): bool&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ showCards(): void&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="I8JL-FB-3HwPmqZuM6Aw-91" vertex="1">
          <mxGeometry x="60" y="115" width="240" height="150" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-3" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;AssemblyProtocol&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- blockedDeck: Deck&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- decksList: Deck[3]&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- hourGlass: HourGlass()&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- coveredList: List&amp;lt;Component&amp;gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- uncoveredList: List&amp;lt;Component&amp;gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- bookedMap: Map&amp;lt;Color, Component[2]&amp;gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- viewMap: Map&amp;lt;Color, Component&amp;gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- occupiedDecks: bool[4]&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ AssemblyProtocol()&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ showDeck(int): Deck&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ newComponent(Color): Component&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ chooseComponent(Color): Component&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ bookComponent(Color, Componente): void&amp;nbsp;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- checkOccupation(Deck): &amp;nbsp;bool&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ addShip(Color, xMat, yMat);&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;br&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;// + metodoAssemblatore runnable, &amp;nbsp;ipotizzo si possa fare con un while(true) che prende in ingresso i comandi e li esegue con i metodi precedenti, usando dei lock, il thread va a terminare quando si completano determinate task di fine assemblaggio&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="I8JL-FB-3HwPmqZuM6Aw-91" vertex="1">
          <mxGeometry x="420" y="105" width="400" height="340" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-4" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;HourGlass&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- state: int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- finished: bool&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- scheduler: ScheduledExecutorService&amp;nbsp;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- life: int&amp;nbsp;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ HourGlass()&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ twist(): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- updateState(): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getState(): int&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="I8JL-FB-3HwPmqZuM6Aw-91" vertex="1">
          <mxGeometry x="60" y="285" width="240" height="160" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-5" style="edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;exitX=0.5;exitY=1;exitDx=0;exitDy=0;" parent="I8JL-FB-3HwPmqZuM6Aw-91" source="7xmkEr-Nzk-kSTEPStux-1" target="7xmkEr-Nzk-kSTEPStux-1" edge="1">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-15" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="I8JL-FB-3HwPmqZuM6Aw-91" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="422" y="195" as="sourcePoint" />
            <mxPoint x="300" y="195" as="targetPoint" />
            <Array as="points">
              <mxPoint x="422" y="195" />
              <mxPoint x="300" y="195" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-16" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;entryX=0.996;entryY=0.435;entryDx=0;entryDy=0;entryPerimeter=0;exitX=0;exitY=0.734;exitDx=0;exitDy=0;exitPerimeter=0;" parent="I8JL-FB-3HwPmqZuM6Aw-91" source="7xmkEr-Nzk-kSTEPStux-3" target="7xmkEr-Nzk-kSTEPStux-4" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="310" y="415" as="sourcePoint" />
            <mxPoint x="420" y="355" as="targetPoint" />
            <Array as="points">
              <mxPoint x="370" y="355" />
              <mxPoint x="370" y="355" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-13" value="&lt;div&gt;&lt;b&gt;ASSEMBLY&lt;/b&gt;&lt;/div&gt;" style="text;html=1;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;fontSize=12;" parent="I8JL-FB-3HwPmqZuM6Aw-91" vertex="1">
          <mxGeometry x="30" y="39" width="120" height="60" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-105" value="" style="shape=folder;fontStyle=1;tabWidth=80;tabHeight=30;tabPosition=left;html=1;boundedLbl=1;whiteSpace=wrap;movable=1;resizable=1;rotatable=1;deletable=1;editable=1;locked=0;connectable=1;" parent="1" vertex="1">
          <mxGeometry x="-12010" y="-90" width="2560" height="970" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-34" value="" style="shape=folder;fontStyle=1;spacingTop=10;tabWidth=40;tabHeight=14;tabPosition=left;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-5740" y="1320" width="1460" height="620" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-145" value="" style="shape=folder;fontStyle=1;spacingTop=10;tabWidth=40;tabHeight=14;tabPosition=left;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-7840" y="-1300" width="5320" height="1640" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-3" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;br&gt;&lt;b&gt;CreditsGain&lt;/b&gt;&lt;/p&gt;&lt;br&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;default giveCredit(player: Player, creditNumber: int): void&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-5809" y="-1060" width="310" height="80" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-4" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;br&gt;&lt;b&gt;GoodsGain&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;div&gt;&amp;nbsp;default giveGoods(player: Player, goods: int[4]): void&lt;/div&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-3938" y="-1060" width="300" height="90" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-14" value="&lt;font style=&quot;font-size: 36px;&quot;&gt;FINAL EVALUATION&lt;/font&gt;" style="text;html=1;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;fontSize=90;" parent="1" vertex="1">
          <mxGeometry x="-6350" y="2620" width="420" height="154" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-15" value="Component" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;fillColor=default;swimlaneFillColor=none;" parent="1" vertex="1">
          <mxGeometry x="-10950" y="60" width="390" height="220" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-16" value="- left: int&lt;div&gt;- right: int&lt;/div&gt;&lt;div&gt;- front: int&lt;/div&gt;&lt;div&gt;- back: int&lt;/div&gt;" style="text;strokeColor=none;fillColor=default;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;swimlaneFillColor=none;" parent="I8JL-FB-3HwPmqZuM6Aw-15" vertex="1">
          <mxGeometry y="26" width="390" height="74" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-17" value="" style="line;strokeWidth=1;fillColor=default;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;swimlaneFillColor=none;" parent="I8JL-FB-3HwPmqZuM6Aw-15" vertex="1">
          <mxGeometry y="100" width="390" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-18" value="&lt;div&gt;+ rotate(): void&lt;/div&gt;&lt;div&gt;+ component()&lt;/div&gt;&lt;div&gt;&lt;div&gt;+ getLeft()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;:&amp;nbsp;&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getRight()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;:&amp;nbsp;&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getFront()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;:&amp;nbsp;&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getBack()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;:&amp;nbsp;&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;/div&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=default;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;swimlaneFillColor=none;" parent="I8JL-FB-3HwPmqZuM6Aw-15" vertex="1">
          <mxGeometry y="108" width="390" height="112" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-8" value="&lt;font style=&quot;font-size: 20px;&quot;&gt;&lt;b&gt;ShipBoard&lt;/b&gt;&lt;/font&gt;" style="text;html=1;align=center;verticalAlign=middle;resizable=0;points=[];autosize=1;strokeColor=none;fillColor=none;" parent="1" vertex="1">
          <mxGeometry x="-5725" y="1340" width="120" height="40" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-19" value="Storage" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-10500" y="420" width="340" height="390" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-20" value="- type: bool&lt;div&gt;- numberOfCurrentElements: int&lt;/div&gt;&lt;div&gt;- numberOfMaximumElements: int&lt;/div&gt;&lt;div&gt;- red: int&lt;/div&gt;&lt;div&gt;- blu: int&lt;/div&gt;&lt;div&gt;- green: int&lt;/div&gt;&lt;div&gt;- yellow: int&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-19" vertex="1">
          <mxGeometry y="26" width="340" height="104" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-21" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-19" vertex="1">
          <mxGeometry y="130" width="340" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-22" value="+ getType(): bool&lt;div&gt;+ getNumberOfCurrentElements()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getNumberOfMaximumElements()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getRed()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getBlue()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getGreen()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getYellow()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ setType(bool): void&lt;div&gt;+ setNumberOfCurrentElements(&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ setNumberOfMaximumElements(&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ setRed(&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ setBlue(&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ setGreen(&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ setYellow(&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;+ storage()&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;- verifyLegalStorage()&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-19" vertex="1">
          <mxGeometry y="138" width="340" height="252" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-2" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;Requirement&lt;/b&gt;&lt;/p&gt;&lt;div&gt;&lt;hr&gt;&amp;nbsp;default isSatisfying(player: Player, requirementType: ElementType, quantity: int): bool&lt;/div&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-4584" y="-1060" width="510" height="70" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-31" value="ScoreCounter" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-5680" y="2890" width="300" height="160" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-32" value="- leastNumberOfLinks:&amp;nbsp; Player" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="T3ZdyJxvqip0CuZ5rjFb-31" vertex="1">
          <mxGeometry y="26" width="300" height="26" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-33" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="T3ZdyJxvqip0CuZ5rjFb-31" vertex="1">
          <mxGeometry y="52" width="300" height="8" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-34" value="&lt;div&gt;+ scoreCounter()&lt;/div&gt;+ calculatePlayerScore(Player): int&lt;div&gt;- calculateFinishingOrderPoints(Player): int&lt;br&gt;&lt;div&gt;- calculateStoragePoints(Player): int&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;- calculateLeastNumberOfLinks(Player): int&amp;nbsp;&lt;/span&gt;&lt;/div&gt;&lt;div&gt;- calculatePointsForLostComponents(&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;Player&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;): int&lt;/span&gt;&lt;/div&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="T3ZdyJxvqip0CuZ5rjFb-31" vertex="1">
          <mxGeometry y="60" width="300" height="100" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-1" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;Movable&lt;/b&gt;&lt;/p&gt;&lt;br&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;default changePlayerPosition(player: Player, dayNumber: int): void&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-5202" y="-1060" width="370" height="90" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-148" value="Adventures" style="text;html=1;align=center;verticalAlign=middle;resizable=0;points=[];autosize=1;strokeColor=none;fillColor=none;fontSize=100;" parent="1" vertex="1">
          <mxGeometry x="-7740" y="-1230" width="530" height="130" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-7" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;SmallestCrew&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&amp;nbsp;default calculateSmallestCrew(void): Player" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-7160" y="-1060" width="290" height="70" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-151" value="Application" style="text;align=center;fontStyle=1;verticalAlign=middle;spacingLeft=3;spacingRight=3;strokeColor=none;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;html=1;fontSize=100;" parent="1" vertex="1">
          <mxGeometry x="-9660" y="1390" width="80" height="26" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-12" value="Planets" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-3260" y="-584" width="220" height="150" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-13" value="&lt;div&gt;-daysLost: int&lt;/div&gt;&lt;div&gt;-planet1: int[4]&lt;/div&gt;&lt;div&gt;-planet2: int[4]&lt;/div&gt;&lt;div&gt;-planet3: int[4]&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-12" vertex="1">
          <mxGeometry y="26" width="220" height="74" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-14" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-12" vertex="1">
          <mxGeometry y="100" width="220" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-15" value="&lt;div&gt;+resolve(player: player): void&lt;/div&gt;&lt;div&gt;+Planets(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-12" vertex="1">
          <mxGeometry y="108" width="220" height="42" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-16" value="AbbandonedShip" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-5190" y="-584" width="200" height="170" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-17" value="&lt;div&gt;-daysLost: int&lt;/div&gt;&lt;div&gt;-lossType: ElementType&lt;/div&gt;&lt;div&gt;-numPerdite: int&lt;/div&gt;&lt;div&gt;-numSoldiGuadagnati: int&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-16" vertex="1">
          <mxGeometry y="26" width="200" height="84" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-18" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-16" vertex="1">
          <mxGeometry y="110" width="200" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-19" value="&lt;div&gt;+resolve(player: player): void&lt;/div&gt;&lt;div&gt;+AbbandonedShip(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-16" vertex="1">
          <mxGeometry y="118" width="200" height="52" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-20" value="AbbandonedStation" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-6170" y="-584" width="220" height="130" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-21" value="&lt;div&gt;-daysLost: int&lt;/div&gt;&lt;div&gt;-requirementType: ElementType&lt;/div&gt;&lt;div&gt;-goods: int[4]&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-20" vertex="1">
          <mxGeometry y="26" width="220" height="54" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-22" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-20" vertex="1">
          <mxGeometry y="80" width="220" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-23" value="&lt;div&gt;+resolve(player: player): void&lt;/div&gt;&lt;div&gt;+AbbandonedShip(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-20" vertex="1">
          <mxGeometry y="88" width="220" height="42" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-24" value="Smugglers" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-5870" y="-584" width="210" height="150" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-25" value="&lt;div&gt;-daysLost: int&lt;/div&gt;&lt;div&gt;-requirementType: ElementType&lt;/div&gt;&lt;div&gt;-lossType: ElementType&lt;/div&gt;&lt;div&gt;-lossQuantity: int&lt;/div&gt;&lt;div&gt;-goods: int[4]&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-24" vertex="1">
          <mxGeometry y="26" width="210" height="74" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-26" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-24" vertex="1">
          <mxGeometry y="100" width="210" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-27" value="&lt;div&gt;+resolve(player: player): void&lt;/div&gt;&lt;div&gt;+Smugglers(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-24" vertex="1">
          <mxGeometry y="108" width="210" height="42" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-28" value="OpenSpace" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-2770" y="-584" width="200" height="90" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-29" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-28" vertex="1">
          <mxGeometry y="26" width="200" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-30" value="&lt;div&gt;+resolve(player: player): void&lt;/div&gt;&lt;div&gt;+OpenSpace(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-28" vertex="1">
          <mxGeometry y="34" width="200" height="56" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-31" value="SmallMeteorSwarm" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7590" y="-584" width="270" height="130" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-32" value="&lt;div&gt;-blowType: ElementType&lt;/div&gt;&lt;div&gt;-meteorites: Blows[3]&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-31" vertex="1">
          <mxGeometry y="26" width="270" height="44" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-33" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-31" vertex="1">
          <mxGeometry y="70" width="270" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-34" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;+SmallMeteorSwarm(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-31" vertex="1">
          <mxGeometry y="78" width="270" height="52" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-35" value="CombatZone" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-6490" y="-584" width="260" height="190" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-36" value="&lt;div&gt;-daysLost: int&lt;/div&gt;&lt;div&gt;-tokenLost: int&lt;/div&gt;&lt;div&gt;-blows: Blows[2]&lt;/div&gt;&lt;div&gt;-tipoBlows: ElementType&lt;/div&gt;&lt;div&gt;-lossType: ElementType&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-35" vertex="1">
          <mxGeometry y="26" width="260" height="84" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-37" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-35" vertex="1">
          <mxGeometry y="110" width="260" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-38" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;-calculateWeakestEnginePower(void): player&lt;/div&gt;&lt;div&gt;-calcolaWeakestFirePower:(void): player&lt;/div&gt;&lt;div&gt;+CombatZone(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-35" vertex="1">
          <mxGeometry y="118" width="260" height="72" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-39" value="StarDust" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-4270" y="-584" width="310" height="90" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-40" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-39" vertex="1">
          <mxGeometry y="26" width="310" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-41" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;-calculateExposedConnectors(Player: player): int&lt;/div&gt;&lt;div&gt;+StarDust(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-39" vertex="1">
          <mxGeometry y="34" width="310" height="56" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-42" value="&lt;div&gt;BigMeteorSwarm&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7200" y="-584" width="270" height="140" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-43" value="&lt;div&gt;-blowType: ElementType&lt;/div&gt;&lt;div&gt;-meteorites: Blows[5]&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-42" vertex="1">
          <mxGeometry y="26" width="270" height="44" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-44" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-42" vertex="1">
          <mxGeometry y="70" width="270" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-45" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;+BigMeteorSwarm(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-42" vertex="1">
          <mxGeometry y="78" width="270" height="62" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-46" value="Slavers" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-5520" y="-584" width="230" height="170" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-47" value="&lt;div&gt;-daysLost: int&lt;/div&gt;&lt;div&gt;-gainedCredits: int&lt;/div&gt;&lt;div&gt;-requirementType: ElementType&lt;/div&gt;&lt;div&gt;-lossType: ElementType&lt;/div&gt;&lt;div&gt;-lossQuantity: int&lt;/div&gt;&lt;div&gt;-requirementNumber: int&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-46" vertex="1">
          <mxGeometry y="26" width="230" height="94" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-48" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-46" vertex="1">
          <mxGeometry y="120" width="230" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-49" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;+Slavers(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-46" vertex="1">
          <mxGeometry y="128" width="230" height="42" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-50" value="Epidemic" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-4940" y="-584" width="570" height="90" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-51" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-50" vertex="1">
          <mxGeometry y="26" width="570" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-52" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;-removeAdjacentAstronauts(Player: player): void&lt;/div&gt;&lt;div&gt;+Epidemic(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-50" vertex="1">
          <mxGeometry y="34" width="570" height="56" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-53" value="Sabotage" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-3660" y="-584" width="320" height="100" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-54" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-53" vertex="1">
          <mxGeometry y="26" width="320" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-55" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;-destroyRandomComponent(giocotare: Player): void&lt;/div&gt;&lt;div&gt;+Sabotage(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-53" vertex="1">
          <mxGeometry y="34" width="320" height="66" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-56" value="Blow(package view only)" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7030" y="100" width="250" height="140" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-57" value="&lt;div&gt;-direction: int&lt;/div&gt;&lt;div&gt;-big: bool&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-56" vertex="1">
          <mxGeometry y="26" width="250" height="44" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-58" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-56" vertex="1">
          <mxGeometry y="70" width="250" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-59" value="&lt;div&gt;+Blow(direction: int, big: bool)&lt;/div&gt;&lt;div&gt;+getDirection(void): int&lt;/div&gt;&lt;div&gt;+getDimension(void): int&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-56" vertex="1">
          <mxGeometry y="78" width="250" height="62" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-60" value="ElementType (Enum)" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=20;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7610" y="100" width="330" height="54" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-61" value="+CrewMember, Meteorite, CannonBlow, Goods, FirePower, EnginePower" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-60" vertex="1">
          <mxGeometry y="20" width="330" height="34" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-62" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-56" target="gsT4-_EyU5koDzllndjB-116" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7570" y="-790" as="sourcePoint" />
            <mxPoint x="-7410" y="-790" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-63" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-56" target="gsT4-_EyU5koDzllndjB-42" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7570" y="-765" as="sourcePoint" />
            <mxPoint x="-7410" y="-765" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-64" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-56" target="gsT4-_EyU5koDzllndjB-31" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7570" y="-745" as="sourcePoint" />
            <mxPoint x="-7410" y="-745" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-65" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-56" target="gsT4-_EyU5koDzllndjB-35" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7660" y="-530" as="sourcePoint" />
            <mxPoint x="-7500" y="-530" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-66" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-116" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6710" y="-960" as="sourcePoint" />
            <mxPoint x="-6550" y="-960" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-6740" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-67" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-42" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6710" y="-960" as="sourcePoint" />
            <mxPoint x="-6550" y="-960" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7105" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-68" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-31" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6710" y="-960" as="sourcePoint" />
            <mxPoint x="-6550" y="-960" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7495" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-69" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-35" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6710" y="-960" as="sourcePoint" />
            <mxPoint x="-6550" y="-960" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-6400" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-70" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-20" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6030" y="-680" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-6100" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-71" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-28" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6030" y="-680" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-2710" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-72" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-24" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6326" y="-2481" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-5805" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-73" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-12" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6030" y="-680" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-3190" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-74" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-16" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6030" y="-680" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-5130" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-75" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-46" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6030" y="-680" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-5445" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-76" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-53" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6030" y="-680" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-3540" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-77" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-50" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6190" y="-680" as="sourcePoint" />
            <mxPoint x="-6262" y="-2590" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-4695" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-78" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-39" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5970" y="-680" as="sourcePoint" />
            <mxPoint x="-5810" y="-680" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-4155" y="-190" />
              <mxPoint x="-6325" y="-190" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-79" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-116" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6870" y="-830" as="sourcePoint" />
            <mxPoint x="-6710" y="-830" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-80" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-116" target="gsT4-_EyU5koDzllndjB-3" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7168" y="-1168" as="sourcePoint" />
            <mxPoint x="-5875" y="-1405" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-81" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-116" target="gsT4-_EyU5koDzllndjB-5" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7158" y="-1158" as="sourcePoint" />
            <mxPoint x="-6202" y="-1410" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-82" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-116" target="gsT4-_EyU5koDzllndjB-2" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7150" y="-1141" as="sourcePoint" />
            <mxPoint x="-6548" y="-1404" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-83" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-12" target="gsT4-_EyU5koDzllndjB-3" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6120" y="-920" as="sourcePoint" />
            <mxPoint x="-5960" y="-920" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-84" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.459;entryY=0.967;entryDx=0;entryDy=0;entryPerimeter=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-12" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6105" y="-1260" as="sourcePoint" />
            <mxPoint x="-6014" y="-1415" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-85" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-42" target="gsT4-_EyU5koDzllndjB-5" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6490" y="-920" as="sourcePoint" />
            <mxPoint x="-6330" y="-920" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-86" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-31" target="gsT4-_EyU5koDzllndjB-5" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7100" y="-886" as="sourcePoint" />
            <mxPoint x="-6476" y="-1402" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-87" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-35" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6490" y="-920" as="sourcePoint" />
            <mxPoint x="-6330" y="-920" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-88" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-35" target="gsT4-_EyU5koDzllndjB-5" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7195" y="-490" as="sourcePoint" />
            <mxPoint x="-5886" y="-1401" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-89" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-35" target="gsT4-_EyU5koDzllndjB-6" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7146" y="-463" as="sourcePoint" />
            <mxPoint x="-6396" y="-1400" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-90" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-28" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6580" y="-840" as="sourcePoint" />
            <mxPoint x="-6420" y="-840" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-91" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-20" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6580" y="-840" as="sourcePoint" />
            <mxPoint x="-6420" y="-840" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-92" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-20" target="gsT4-_EyU5koDzllndjB-2" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6585" y="-450" as="sourcePoint" />
            <mxPoint x="-5679" y="-1409" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-93" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-20" target="gsT4-_EyU5koDzllndjB-4" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6546" y="-448" as="sourcePoint" />
            <mxPoint x="-5546" y="-1410" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-94" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-24" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6100" y="-810" as="sourcePoint" />
            <mxPoint x="-5940" y="-810" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-95" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-24" target="gsT4-_EyU5koDzllndjB-2" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6100" y="-810" as="sourcePoint" />
            <mxPoint x="-5940" y="-810" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-96" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-24" target="gsT4-_EyU5koDzllndjB-4" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5932" y="-355" as="sourcePoint" />
            <mxPoint x="-5360" y="-1400" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-97" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-24" target="gsT4-_EyU5koDzllndjB-6" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5850" y="-350" as="sourcePoint" />
            <mxPoint x="-3940" y="-1510" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-98" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-46" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5690" y="-695" as="sourcePoint" />
            <mxPoint x="-5740" y="-1300" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-99" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-46" target="gsT4-_EyU5koDzllndjB-2" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5422" y="-350" as="sourcePoint" />
            <mxPoint x="-5790" y="-1405" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-100" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-46" target="gsT4-_EyU5koDzllndjB-3" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5365" y="-350" as="sourcePoint" />
            <mxPoint x="-6500" y="-1540" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-101" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-46" target="gsT4-_EyU5koDzllndjB-6" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5307" y="-350" as="sourcePoint" />
            <mxPoint x="-6125" y="-1410" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-102" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-16" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5790" y="-870" as="sourcePoint" />
            <mxPoint x="-5630" y="-870" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-103" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-16" target="gsT4-_EyU5koDzllndjB-3" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5400" y="-950" as="sourcePoint" />
            <mxPoint x="-5705" y="-1405" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-104" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-16" target="gsT4-_EyU5koDzllndjB-6" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5390" y="-940" as="sourcePoint" />
            <mxPoint x="-5180" y="-1040" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-105" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-39" target="gsT4-_EyU5koDzllndjB-1" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-4348.27" y="-646.04" as="sourcePoint" />
            <mxPoint x="-5970" y="-1510" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-106" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-116" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6840" y="-510" as="sourcePoint" />
            <mxPoint x="-6680" y="-510" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-107" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-42" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6425" y="90" as="sourcePoint" />
            <mxPoint x="-7182" y="-1108" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-108" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-31" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7491.89" y="550" as="sourcePoint" />
            <mxPoint x="-7540" y="-483.0159999999996" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-109" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-35" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6590" y="90" as="sourcePoint" />
            <mxPoint x="-7268" y="-593" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-110" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-20" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6590" y="127" as="sourcePoint" />
            <mxPoint x="-7130" y="-336" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-111" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-24" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6840" y="-630" as="sourcePoint" />
            <mxPoint x="-6680" y="-630" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-112" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-46" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6400" y="-780" as="sourcePoint" />
            <mxPoint x="-6240" y="-780" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-113" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-60" target="gsT4-_EyU5koDzllndjB-16" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6610" y="-533" as="sourcePoint" />
            <mxPoint x="-5700" y="-327" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-114" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-116" target="gsT4-_EyU5koDzllndjB-7" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-6570" y="-990" as="sourcePoint" />
            <mxPoint x="-6410" y="-990" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-115" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-53" target="gsT4-_EyU5koDzllndjB-7" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-5430" y="-1220" as="sourcePoint" />
            <mxPoint x="-6865" y="-1420" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-116" value="Pirates" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-6820" y="-584" width="240" height="190" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-117" value="&lt;div&gt;-daysLost: int&lt;/div&gt;&lt;div&gt;-gainedCredits: int&lt;/div&gt;&lt;div&gt;-requirementType: ElementType&lt;/div&gt;&lt;div&gt;-requirementNumber: int&lt;/div&gt;&lt;div&gt;-tipoBlows: ElementType&lt;/div&gt;&lt;div&gt;-Blows: Blows[5]&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-116" vertex="1">
          <mxGeometry y="26" width="240" height="94" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-118" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-116" vertex="1">
          <mxGeometry y="120" width="240" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-119" value="&lt;div&gt;+resolve(player: Player): void&lt;/div&gt;&lt;div&gt;+Pirates(void)&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-116" vertex="1">
          <mxGeometry y="128" width="240" height="62" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-120" value="Bank" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-6170" y="2850" width="410" height="360" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-121" value="&lt;div&gt;-goods: int[4]&lt;/div&gt;&lt;div&gt;-creditNumbers: int&lt;/div&gt;&lt;div&gt;-astronautNumbers: int&lt;/div&gt;&lt;div&gt;-PurpleAliensNumber: int&lt;/div&gt;&lt;div&gt;-BrownAlinesNumber: int&lt;/div&gt;&lt;div&gt;-batteriesNumber: int&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-120" vertex="1">
          <mxGeometry y="26" width="410" height="104" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-122" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-120" vertex="1">
          <mxGeometry y="130" width="410" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-123" value="+Bank(alieniViola: int, alieniMarroni)&lt;div&gt;+removeBatteries(quantita: int): bool&lt;/div&gt;&lt;div&gt;+removeCrew(quantita: int): bool&lt;/div&gt;&lt;div&gt;+removeGoods(merce: int[4]): bool[4]&lt;/div&gt;&lt;div&gt;+removeCredits(quantita: int): bool&lt;/div&gt;&lt;div&gt;+removePurpleAlliens(quantita: int): bool&lt;/div&gt;&lt;div&gt;+removeBrownAlliens(quantita: int): bool&lt;/div&gt;&lt;div&gt;&lt;div&gt;+addBatteries(quantita: int): void&lt;/div&gt;&lt;div&gt;+addCrew(quantita: int): void&lt;/div&gt;&lt;div&gt;+addGoods(merce: int[4]): void&lt;/div&gt;&lt;div&gt;+addCredits(quantita: int): void&lt;/div&gt;&lt;div&gt;+addPurpleAlliens(quantita: int): void&lt;/div&gt;&lt;div&gt;+addBrownAlliens(quantity: int): void&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;br&gt;&lt;/div&gt;&lt;br&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-120" vertex="1">
          <mxGeometry y="138" width="410" height="222" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-1" value="FlightMechanism" style="swimlane;fontStyle=0;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeLast=0;collapsible=1;marginBottom=0;rounded=0;shadow=0;strokeWidth=1;" parent="1" vertex="1">
          <mxGeometry x="-8850" y="660" width="240" height="180" as="geometry">
            <mxRectangle x="-2" y="180" width="160" height="26" as="alternateBounds" />
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-2" value="-deck: Deck" style="text;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;rounded=0;shadow=0;html=0;" parent="I8JL-FB-3HwPmqZuM6Aw-1" vertex="1">
          <mxGeometry y="26" width="240" height="34" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-3" value="" style="line;html=1;strokeWidth=1;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;" parent="I8JL-FB-3HwPmqZuM6Aw-1" vertex="1">
          <mxGeometry y="60" width="240" height="20" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-4" value="&lt;div&gt;&lt;div&gt;+startFlightMechanism(deck: Deck)&lt;/div&gt;&lt;/div&gt;&lt;div&gt;-shuffleCards(deck: Deck): Deck&lt;/div&gt;&lt;div&gt;-getNewCard(deck: Deck): Card&lt;/div&gt;&lt;div&gt;-executeCard(card: Card)&lt;/div&gt;&lt;div&gt;-endFlightMechanism()&lt;/div&gt;&lt;div&gt;&amp;nbsp;&lt;/div&gt;" style="text;html=1;align=left;verticalAlign=middle;whiteSpace=wrap;rounded=0;" parent="I8JL-FB-3HwPmqZuM6Aw-1" vertex="1">
          <mxGeometry y="80" width="240" height="100" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-5" value="FlightBoard" style="swimlane;fontStyle=0;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeLast=0;collapsible=1;marginBottom=0;rounded=0;shadow=0;strokeWidth=1;" parent="1" vertex="1">
          <mxGeometry x="-9180" y="660" width="300" height="230" as="geometry">
            <mxRectangle x="-2" y="180" width="160" height="26" as="alternateBounds" />
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-6" value="-numberOfTiles: int {readOnly}&#xa;-playerTilesMap: Map&lt;Player, Integer&gt;&#xa;-playerOrderList: Player[4]" style="text;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;rounded=0;shadow=0;html=0;" parent="I8JL-FB-3HwPmqZuM6Aw-5" vertex="1">
          <mxGeometry y="26" width="300" height="54" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-7" value="" style="line;html=1;strokeWidth=1;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;" parent="I8JL-FB-3HwPmqZuM6Aw-5" vertex="1">
          <mxGeometry y="80" width="300" height="20" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-8" value="+createFlightBoard(numberOfTiles: int): FlightBoard&lt;div&gt;+setPlayerTile(player: Player, tile: int)&lt;/div&gt;&lt;div&gt;+incrementPlayerTile(player: Player, tiles: int)&lt;/div&gt;&lt;div&gt;+getPlayerTile(player: Player): int&lt;br&gt;&lt;div&gt;&lt;div&gt;+getPlayerInOrder(order: int): Player&lt;/div&gt;&lt;div&gt;+updatePlayerOrder()&lt;/div&gt;&lt;div&gt;+checkLappedPlayer(): Player&lt;/div&gt;&lt;/div&gt;&lt;div&gt;+eliminatePlayer(player: Player)&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;/div&gt;" style="text;html=1;align=left;verticalAlign=middle;whiteSpace=wrap;rounded=0;" parent="I8JL-FB-3HwPmqZuM6Aw-5" vertex="1">
          <mxGeometry y="100" width="300" height="130" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-39" value="AlienSupport" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-11950" y="420" width="160" height="110" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-40" value="- purple: bool" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-39" vertex="1">
          <mxGeometry y="26" width="160" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-41" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-39" vertex="1">
          <mxGeometry y="52" width="160" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-42" value="+ getPurple(): bool&lt;div&gt;+ setPurple(bool&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;+ alienSupport(): void&lt;/span&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-39" vertex="1">
          <mxGeometry y="60" width="160" height="50" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-11" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;Player&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- nickname: String&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- color: Color&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- plancia: ShipBoard&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ Player(String nick)&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getName(): String&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getColor(): Color&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;br&gt;&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-4770" y="1395" width="370" height="360" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-50" value="0 = no connector, 1 = 1 connector, ..., 4 = jolly connector" style="text;html=1;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;" parent="1" vertex="1">
          <mxGeometry x="-11110" y="30" width="315" height="25" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-51" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-39" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-10420" y="300" as="sourcePoint" />
            <mxPoint x="-10180" y="140" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-53" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-44" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-10410" y="610" as="sourcePoint" />
            <mxPoint x="-10250" y="610" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-54" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-31" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-10410" y="480" as="sourcePoint" />
            <mxPoint x="-10250" y="480" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-10740" y="360" />
              <mxPoint x="-10755" y="360" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-55" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-35" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-10030" y="360" as="sourcePoint" />
            <mxPoint x="-10370" y="220" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-9845" y="170" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-56" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-19" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-10150" y="-210" as="sourcePoint" />
            <mxPoint x="-10490" y="-90" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-10210" y="420" />
              <mxPoint x="-10210" y="350" />
              <mxPoint x="-10755" y="350" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-44" value="Shield" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-11130" y="420" width="160" height="110" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-45" value="nessuno" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-44" vertex="1">
          <mxGeometry y="26" width="160" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-46" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-44" vertex="1">
          <mxGeometry y="52" width="160" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-47" value="&lt;div&gt;+ shield()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-44" vertex="1">
          <mxGeometry y="60" width="160" height="50" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-59" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-27" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-10770" y="600" as="sourcePoint" />
            <mxPoint x="-10610" y="600" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-11430" y="370" />
              <mxPoint x="-10755" y="370" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-43" value="&lt;span style=&quot;font-size: 36px;&quot;&gt;&lt;b&gt;Components&lt;/b&gt;&lt;/span&gt;" style="text;html=1;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;" parent="1" vertex="1">
          <mxGeometry x="-11991" y="-40" width="410" height="85" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-60" value="Extends" style="endArrow=block;endSize=16;endFill=0;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-23" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-10770" y="600" as="sourcePoint" />
            <mxPoint x="-10610" y="600" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-11325" y="370" />
              <mxPoint x="-10755" y="370" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-31" value="Battery" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-10890" y="420" width="300" height="110" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-32" value="- numberOfCurrentElements: int" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-31" vertex="1">
          <mxGeometry y="26" width="300" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-33" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-31" vertex="1">
          <mxGeometry y="52" width="300" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-34" value="+ getNumberOfCurrentElements():&amp;nbsp;&lt;span style=&quot;color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255)); background-color: transparent;&quot;&gt;int&lt;/span&gt;&lt;div&gt;+ setN&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;umberOfCurrentElements(&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;+ battery(): void&lt;/span&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-31" vertex="1">
          <mxGeometry y="60" width="300" height="50" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-17" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;entryX=1.003;entryY=0.235;entryDx=0;entryDy=0;entryPerimeter=0;exitX=-0.002;exitY=0.332;exitDx=0;exitDy=0;exitPerimeter=0;" parent="1" source="7xmkEr-Nzk-kSTEPStux-10" target="7xmkEr-Nzk-kSTEPStux-9" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-5290" y="1575" as="sourcePoint" />
            <mxPoint x="-5250" y="1515" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-5290" y="1515" />
              <mxPoint x="-5290" y="1515" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-23" value="&lt;div&gt;Cannon&lt;/div&gt;" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-11410" y="420" width="170" height="110" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-24" value="- single: bool" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-23" vertex="1">
          <mxGeometry y="26" width="170" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-25" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-23" vertex="1">
          <mxGeometry y="52" width="170" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-26" value="+ getSingle(): bool&lt;div&gt;+ setSingle(bool&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;+ cannon()&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-23" vertex="1">
          <mxGeometry y="60" width="170" height="50" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-18" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;exitX=0.214;exitY=1.001;exitDx=0;exitDy=0;exitPerimeter=0;" parent="1" source="7xmkEr-Nzk-kSTEPStux-11" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-4670" y="1875" as="sourcePoint" />
            <mxPoint x="-5329" y="1835" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-4691" y="1835" />
              <mxPoint x="-5329" y="1835" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-27" value="Propulsor" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-11680" y="420" width="160" height="110" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-28" value="- single: bool" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-27" vertex="1">
          <mxGeometry y="26" width="160" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-29" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-27" vertex="1">
          <mxGeometry y="52" width="160" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-30" value="+ getSingle(): bool&lt;div&gt;+ setSingle(bool): void&lt;/div&gt;&lt;div&gt;+ propulsor()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-27" vertex="1">
          <mxGeometry y="60" width="160" height="50" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-127" value="Application" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-8540" y="1940" width="230" height="210" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-28" value="&lt;div style=&quot;text-align: left;&quot;&gt;+ gameInformation: GameInformation&lt;/div&gt;&lt;div style=&quot;text-align: left;&quot;&gt;+ assemblingPhase: AssemblingPhase&lt;/div&gt;&lt;div style=&quot;text-align: left;&quot;&gt;+ flightPhase: FlightPhase&lt;/div&gt;&lt;div style=&quot;text-align: left;&quot;&gt;+ setupPhase: SetupPhase&lt;/div&gt;&lt;div style=&quot;text-align: left;&quot;&gt;+ initializationPhase: InitializationPhase&lt;/div&gt;&lt;div style=&quot;text-align: left;&quot;&gt;+ evaluationPhase: EvaluationPhase&lt;/div&gt;&lt;div style=&quot;text-align: left;&quot;&gt;+gameEnded: bool&lt;/div&gt;" style="text;html=1;align=center;verticalAlign=middle;resizable=0;points=[];autosize=1;strokeColor=none;fillColor=none;" parent="gsT4-_EyU5koDzllndjB-127" vertex="1">
          <mxGeometry y="26" width="230" height="110" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-129" value="&#xa;" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-127" vertex="1">
          <mxGeometry y="136" width="230" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-130" value="+main(void): void" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-127" vertex="1">
          <mxGeometry y="144" width="230" height="66" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-131" value="AssemblingPhase(Controller)" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-8645" y="2650" width="290" height="86" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-132" value="+ assemblyProtocol: AssemblyProtocol" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-131" vertex="1">
          <mxGeometry y="26" width="290" height="26" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-133" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-131" vertex="1">
          <mxGeometry y="52" width="290" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-134" value="+start(gameInformation: GameInformation): void&lt;div&gt;&lt;br&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-131" vertex="1">
          <mxGeometry y="60" width="290" height="26" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-135" value="FlightPhase(Controller)" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7905" y="2640" width="340" height="90" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-136" value="+ flightMechanism: FlightMechanism" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-135" vertex="1">
          <mxGeometry y="30" width="340" height="26" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-137" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-135" vertex="1">
          <mxGeometry y="56" width="340" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-138" value="+start(gameInformation: Game Information): void" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-135" vertex="1">
          <mxGeometry y="64" width="340" height="26" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-139" value="EvaluationPhase(Controller)" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7275" y="2640" width="400" height="86" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-140" value="+ field: type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-139" vertex="1">
          <mxGeometry y="26" width="400" height="26" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-141" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-139" vertex="1">
          <mxGeometry y="52" width="400" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-142" value="+ start(gameInformation: GameInformation): void" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-139" vertex="1">
          <mxGeometry y="60" width="400" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-66" value="AssemblingView" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-8580" y="2880" width="160" height="86" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-67" value="+ field: type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-66" vertex="1">
          <mxGeometry y="26" width="160" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-68" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-66" vertex="1">
          <mxGeometry y="52" width="160" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-69" value="+" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-66" vertex="1">
          <mxGeometry y="60" width="160" height="26" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-70" value="EvaluationView" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7160" y="2870" width="170" height="86" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-71" value="+ field: type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="T3ZdyJxvqip0CuZ5rjFb-70" vertex="1">
          <mxGeometry y="26" width="170" height="26" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-72" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="T3ZdyJxvqip0CuZ5rjFb-70" vertex="1">
          <mxGeometry y="52" width="170" height="8" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-73" value="+ method(type): type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="T3ZdyJxvqip0CuZ5rjFb-70" vertex="1">
          <mxGeometry y="60" width="170" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-78" value="FlightView" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-7765" y="2870" width="160" height="86" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-79" value="+ field: type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-78" vertex="1">
          <mxGeometry y="26" width="160" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-80" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-78" vertex="1">
          <mxGeometry y="52" width="160" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-81" value="+ method(type): type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-78" vertex="1">
          <mxGeometry y="60" width="160" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-82" value="InitializationPhase(Controller)" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-9265" y="2650" width="270" height="110" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-83" value="+ field: type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-82" vertex="1">
          <mxGeometry y="26" width="270" height="26" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-84" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-82" vertex="1">
          <mxGeometry y="52" width="270" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-85" value="+ start(gameInformation: GameInformation): void" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-82" vertex="1">
          <mxGeometry y="60" width="270" height="50" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-75" value="SetupPhase(Controller)" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-9740" y="2650" width="270" height="110" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-76" value="+ field: type" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="T3ZdyJxvqip0CuZ5rjFb-75" vertex="1">
          <mxGeometry y="26" width="270" height="34" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-77" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="T3ZdyJxvqip0CuZ5rjFb-75" vertex="1">
          <mxGeometry y="60" width="270" height="8" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-78" value="+start(gameInformation: GameInformation) : void" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="T3ZdyJxvqip0CuZ5rjFb-75" vertex="1">
          <mxGeometry y="68" width="270" height="42" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-144" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;br&gt;&lt;b&gt;Startable&lt;/b&gt;&lt;/p&gt;&lt;i&gt;&lt;br&gt;&lt;/i&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;i&gt;+start(GameInformation): void&lt;/i&gt;&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-9275" y="2297" width="190" height="86" as="geometry" />
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-84" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="T3ZdyJxvqip0CuZ5rjFb-75" target="gsT4-_EyU5koDzllndjB-144" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-8915" y="2640" as="sourcePoint" />
            <mxPoint x="-9075" y="2640" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-20" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-131" target="gsT4-_EyU5koDzllndjB-144" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-8485" y="2550" as="sourcePoint" />
            <mxPoint x="-8645" y="2550" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-21" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-139" target="gsT4-_EyU5koDzllndjB-144" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-8765" y="2790" as="sourcePoint" />
            <mxPoint x="-8925" y="2790" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7075" y="2550" />
              <mxPoint x="-9180" y="2550" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-22" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="I8JL-FB-3HwPmqZuM6Aw-82" target="gsT4-_EyU5koDzllndjB-144" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-8695" y="2630" as="sourcePoint" />
            <mxPoint x="-8855" y="2630" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-23" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-135" target="gsT4-_EyU5koDzllndjB-144" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-8985" y="2830" as="sourcePoint" />
            <mxPoint x="-9145" y="2830" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7735" y="2550" />
              <mxPoint x="-9180" y="2550" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-87" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="gsT4-_EyU5koDzllndjB-131" target="I8JL-FB-3HwPmqZuM6Aw-66" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-8305" y="2880" as="sourcePoint" />
            <mxPoint x="-8145" y="2880" as="targetPoint" />
            <Array as="points" />
          </mxGeometry>
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-90" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="gsT4-_EyU5koDzllndjB-135" target="I8JL-FB-3HwPmqZuM6Aw-78" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-8185" y="2780" as="sourcePoint" />
            <mxPoint x="-8025" y="2780" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7735" y="2850" />
              <mxPoint x="-7735" y="2850" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="T3ZdyJxvqip0CuZ5rjFb-93" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="gsT4-_EyU5koDzllndjB-139" target="T3ZdyJxvqip0CuZ5rjFb-70" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-7555" y="2700" as="sourcePoint" />
            <mxPoint x="-7395" y="2700" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7075" y="2830" />
              <mxPoint x="-7075" y="2830" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-5" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;br&gt;&lt;b&gt;SuffersBlows&lt;/b&gt;&lt;/p&gt;&lt;br&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;default hit(player: Player, blow: Blow[5], Type: ElementType): void&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-6442" y="-1060" width="445" height="80" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-6" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;br&gt;&lt;b&gt;TokenLoss&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;default inflictLoss(player: Player, lossType: ElementType, quantity: int): void&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-3510" y="-1060" width="440" height="70" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-9" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;ShipBoard&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- shipStructure:&amp;nbsp;&lt;span style=&quot;text-align: center;&quot;&gt;ShipStructure&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- drivingPower: int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-&lt;span style=&quot;white-space: pre;&quot;&gt;&amp;nbsp;firePower: float&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- crewMembers: int&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- purpleAlien: bool&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- brownAlien: bool&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- batteryPower: int&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- coveredSides: bool[4]&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- availableRedSlots: int &lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- availableBluSlots: int&lt;/span&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;span style=&quot;white-space: pre;&quot;&gt;- destroyedComponents&lt;/span&gt;: int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;br&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ ShipBoard()&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateFirePower(float): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateDrivingPower(int): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateCrew(int): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateAllien(ColoreAlieno): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateBatteries(int): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateShield(int): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateSlots(type, int): void&amp;nbsp;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ updateDestroyedComponents(int): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getDrivingPower (): int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getFirePower(): float&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getNumbersCrew(): int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getAllienType(): int //restituisce 2 se è viola, 1 se marrone e 0 se non c&#39;è&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ getBatteryPower(): int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ checkSide(int): bool&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ checkRedSlots(): bool //controlla che ci siano ancora slot rossi&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+checkBluSlots(): bool&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+getDestroyedComponents(): int&amp;nbsp;&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-5570" y="1395" width="240" height="510" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-35" value="Cabins" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-10070" y="420" width="450" height="180" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-36" value="- type: enum( abitazioneViola, abitazioneMarrone, abitazioneSemplice )&lt;div&gt;- numberOfInhabitants: int&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-35" vertex="1">
          <mxGeometry y="26" width="450" height="34" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-37" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="I8JL-FB-3HwPmqZuM6Aw-35" vertex="1">
          <mxGeometry y="60" width="450" height="8" as="geometry" />
        </mxCell>
        <mxCell id="I8JL-FB-3HwPmqZuM6Aw-38" value="+ getType()&lt;div&gt;+ setType()&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;+ getN&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;umberOfInhabitants()&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;:&amp;nbsp;&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;+ setN&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;umberOfInhabitants(&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;int&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;)&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&lt;/span&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;+ removeInhabitants(): void&amp;nbsp;&amp;nbsp;&lt;/span&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;+ housing()&lt;/span&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;: void&amp;nbsp; &amp;nbsp; &amp;nbsp;&lt;/span&gt;&lt;/div&gt;&lt;div&gt;&lt;span style=&quot;background-color: transparent; color: light-dark(rgb(0, 0, 0), rgb(255, 255, 255));&quot;&gt;&amp;nbsp; //i metodi di riduzione dell&#39;equipaggio(epidemia) penso dovremo metterli in meccanismo di volo&lt;/span&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="I8JL-FB-3HwPmqZuM6Aw-35" vertex="1">
          <mxGeometry y="68" width="450" height="112" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-10" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;ShipStructure&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- structureMatrix: Component[][]&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ ShipStructure()&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;//serviranno prob metodi per riempire la nave&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ addComponent(Componente, int, int): void // gli int sono le coordinate&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ removeComponent(int, int): void&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ countExternalJunctions(): int&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ checkErrors(): void //controlla ogni tipo di errore e in caso rimuove il componente&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ checkNotReachable(): void //metodo ricorsivo che controlla che non ci siano elementi staccati e in caso gli elimina&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;- checkCorrectJunctions(int x, int y): bool //riceve in ingresso le coordinate del blocco e controlla che i blocchi adiacenti abbiano i collegamenti adatti&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;br&gt;&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-5250" y="1395" width="400" height="360" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-8" value="&lt;i&gt;Card&lt;/i&gt;" style="swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=26;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;whiteSpace=wrap;html=1;" parent="1" vertex="1">
          <mxGeometry x="-6430" y="100" width="210" height="162" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-9" value="&lt;div&gt;#level: int&lt;/div&gt;&lt;div&gt;#cardName: String&lt;/div&gt;&lt;div&gt;#solved: bool&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-8" vertex="1">
          <mxGeometry y="26" width="210" height="56" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-10" value="" style="line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;strokeColor=inherit;" parent="gsT4-_EyU5koDzllndjB-8" vertex="1">
          <mxGeometry y="82" width="210" height="8" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-11" value="&lt;div&gt;&lt;i&gt;+resolve(player: Player) : void&lt;/i&gt;&lt;/div&gt;&lt;div&gt;+isSolved(void): bool&lt;/div&gt;&lt;div&gt;+getLevel(void): int&lt;/div&gt;&lt;div&gt;+getCardName(void): String&lt;/div&gt;" style="text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;whiteSpace=wrap;html=1;" parent="gsT4-_EyU5koDzllndjB-8" vertex="1">
          <mxGeometry y="90" width="210" height="72" as="geometry" />
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-35" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;GameInformation&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-cardsList: Card[]&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-componentsList: Component[]&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-playersList: Player[]&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-bank: Bank&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-flightBoard: FlightBoard&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-gameType: GameType&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;-viewType: ViewType&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;div&gt;+getCardsList(void): Card[]&lt;/div&gt;&lt;div&gt;+getComponents(void): Component[]&lt;/div&gt;&lt;div&gt;+getPlayersList(void): Player[]&lt;/div&gt;&lt;div&gt;+getBank(void): Bank&lt;/div&gt;&lt;div&gt;+getFlightBoard(void): FlightBoard&lt;/div&gt;&lt;div&gt;+getViewType(void): void&lt;/div&gt;&lt;div&gt;+setUpCards(gameType: GameType): void&lt;/div&gt;&lt;div&gt;+setUpComponents(void): void&lt;/div&gt;&lt;div&gt;+setUpPlayers(void): void&lt;/div&gt;&lt;div&gt;+setUpBank(void): void&lt;/div&gt;&lt;div&gt;+setUpFlightBoard(gameType: GameType): void&lt;/div&gt;&lt;div&gt;+setUpGameType(gameType: GameType): void&lt;/div&gt;&lt;div&gt;+setViewType(ViewType): void&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;&lt;div&gt;&lt;br&gt;&lt;/div&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-8600" y="1350" width="350" height="360" as="geometry" />
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-1" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;&amp;lt;Enumeration&amp;gt;&lt;/b&gt;&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;GameType&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+GameTest , NormalGame&lt;/p&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;&lt;br&gt;&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-8870" y="1450" width="160" height="70" as="geometry" />
        </mxCell>
        <mxCell id="bZ6PwGqoQkI9PxRKs0EY-2" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;edgeStyle=orthogonalEdgeStyle;exitX=1;exitY=0.5;exitDx=0;exitDy=0;entryX=0;entryY=0.25;entryDx=0;entryDy=0;" parent="1" source="Ru8jyAiUZzCANUr1jurh-1" target="7xmkEr-Nzk-kSTEPStux-35" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-8717.5" y="1795" as="sourcePoint" />
            <mxPoint x="-8607.5" y="1840" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-8" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;b&gt;SetUpView&lt;/b&gt;&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ field: Type&lt;/p&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+ method(): Type&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-9685" y="2880" width="160" height="90" as="geometry" />
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-14" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="T3ZdyJxvqip0CuZ5rjFb-75" target="Ru8jyAiUZzCANUr1jurh-8" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-9135" y="2930" as="sourcePoint" />
            <mxPoint x="-8975" y="2930" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="7xmkEr-Nzk-kSTEPStux-36" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;exitX=0.503;exitY=1.024;exitDx=0;exitDy=0;exitPerimeter=0;" parent="1" source="gsT4-_EyU5koDzllndjB-130" target="gsT4-_EyU5koDzllndjB-135" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-8030" y="2450" as="sourcePoint" />
            <mxPoint x="-7785" y="2630" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8424" y="2500" />
              <mxPoint x="-7685" y="2500" />
            </Array>
            <mxPoint as="offset" />
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-18" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-131" target="gsT4-_EyU5koDzllndjB-152" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-9205" y="2540" as="sourcePoint" />
            <mxPoint x="-9045" y="2540" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8500" y="2550" />
              <mxPoint x="-9495" y="2550" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-16" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="T3ZdyJxvqip0CuZ5rjFb-75" target="gsT4-_EyU5koDzllndjB-152" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-9205" y="2540" as="sourcePoint" />
            <mxPoint x="-9045" y="2540" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-9605" y="2550" />
              <mxPoint x="-9495" y="2550" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-19" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-135" target="gsT4-_EyU5koDzllndjB-152" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-8605" y="2540" as="sourcePoint" />
            <mxPoint x="-8445" y="2540" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7735" y="2550" />
              <mxPoint x="-9495" y="2550" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-152" value="&lt;p style=&quot;margin:0px;margin-top:4px;text-align:center;&quot;&gt;&lt;i&gt;&amp;lt;&amp;lt;Interface&amp;gt;&amp;gt;&lt;/i&gt;&lt;br&gt;&lt;b&gt;Viewable&lt;/b&gt;&lt;/p&gt;&lt;br&gt;&lt;hr size=&quot;1&quot; style=&quot;border-style:solid;&quot;&gt;&lt;p style=&quot;margin:0px;margin-left:4px;&quot;&gt;+updateView(viewType: ViewType): void&lt;/p&gt;" style="verticalAlign=top;align=left;overflow=fill;html=1;whiteSpace=wrap;" parent="1" vertex="1">
          <mxGeometry x="-9610" y="2300" width="230" height="80" as="geometry" />
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-20" value="" style="endArrow=block;dashed=1;endFill=0;endSize=12;html=1;rounded=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;" parent="1" source="gsT4-_EyU5koDzllndjB-139" target="gsT4-_EyU5koDzllndjB-152" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-7475" y="2440" as="sourcePoint" />
            <mxPoint x="-7315" y="2440" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7075" y="2550" />
              <mxPoint x="-9495" y="2550" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-21" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="gsT4-_EyU5koDzllndjB-127" target="gsT4-_EyU5koDzllndjB-139" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-7865" y="2390" as="sourcePoint" />
            <mxPoint x="-7705" y="2390" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8425" y="2500" />
              <mxPoint x="-7025" y="2500" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-22" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="gsT4-_EyU5koDzllndjB-127" target="gsT4-_EyU5koDzllndjB-131" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-8305" y="2684.33" as="sourcePoint" />
            <mxPoint x="-8145" y="2684.33" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8425" y="2500" />
              <mxPoint x="-8445" y="2500" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-23" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="gsT4-_EyU5koDzllndjB-127" target="I8JL-FB-3HwPmqZuM6Aw-82" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-9035" y="2470" as="sourcePoint" />
            <mxPoint x="-8875" y="2470" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8425" y="2500" />
              <mxPoint x="-9065" y="2500" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-24" value="" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="gsT4-_EyU5koDzllndjB-127" target="T3ZdyJxvqip0CuZ5rjFb-75" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-9035" y="2470" as="sourcePoint" />
            <mxPoint x="-8875" y="2470" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8425" y="2500" />
              <mxPoint x="-9545" y="2500" />
            </Array>
            <mxPoint as="offset" />
          </mxGeometry>
        </mxCell>
        <mxCell id="HjN6oJ3qzjS9kNq-yB3L-2" value="FlightManager" style="text;html=1;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;" parent="1" vertex="1">
          <mxGeometry x="-9150" y="580" width="60" height="30" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-153" value="" style="endArrow=diamondThin;endFill=1;endSize=24;html=1;rounded=0;edgeStyle=orthogonalEdgeStyle;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" parent="1" source="7xmkEr-Nzk-kSTEPStux-3" target="gsT4-_EyU5koDzllndjB-131" edge="1">
          <mxGeometry width="160" relative="1" as="geometry">
            <mxPoint x="-9180" y="2492" as="sourcePoint" />
            <mxPoint x="-8670" y="1920" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8693" y="3345" />
              <mxPoint x="-8693" y="2700" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-25" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="7xmkEr-Nzk-kSTEPStux-35" target="I8JL-FB-3HwPmqZuM6Aw-15" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-8850" y="820" as="sourcePoint" />
            <mxPoint x="-9010" y="820" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8425" />
              <mxPoint x="-10755" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-26" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="7xmkEr-Nzk-kSTEPStux-35" target="gsT4-_EyU5koDzllndjB-8" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-7870" y="1450" as="sourcePoint" />
            <mxPoint x="-7710" y="1450" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8425" y="1180" />
              <mxPoint x="-6325" y="1180" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="Ru8jyAiUZzCANUr1jurh-27" value="" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;" parent="1" source="7xmkEr-Nzk-kSTEPStux-35" target="7xmkEr-Nzk-kSTEPStux-11" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-7140" y="1450" as="sourcePoint" />
            <mxPoint x="-6980" y="1450" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-8425" y="1250" />
              <mxPoint x="-4585" y="1250" />
            </Array>
          </mxGeometry>
        </mxCell>
        <mxCell id="HjN6oJ3qzjS9kNq-yB3L-4" value="name" style="endArrow=block;endFill=1;html=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=top;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-127" target="7xmkEr-Nzk-kSTEPStux-35" edge="1">
          <mxGeometry x="-1" relative="1" as="geometry">
            <mxPoint x="-8470" y="1750" as="sourcePoint" />
            <mxPoint x="-8310" y="1750" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="HjN6oJ3qzjS9kNq-yB3L-5" value="1" style="edgeLabel;resizable=0;html=1;align=left;verticalAlign=bottom;" parent="HjN6oJ3qzjS9kNq-yB3L-4" connectable="0" vertex="1">
          <mxGeometry x="-1" relative="1" as="geometry" />
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-156" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;entryX=0;entryY=0;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-120" target="7xmkEr-Nzk-kSTEPStux-35" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-11210" y="2280" as="sourcePoint" />
            <mxPoint x="-11050" y="2280" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-157" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;entryX=0.5;entryY=0;entryDx=0;entryDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-139" target="T3ZdyJxvqip0CuZ5rjFb-31" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-10570" y="2000" as="sourcePoint" />
            <mxPoint x="-11110" y="2160" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-158" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;exitX=0.25;exitY=0;exitDx=0;exitDy=0;entryX=0.5;entryY=1;entryDx=0;entryDy=0;" parent="1" source="7xmkEr-Nzk-kSTEPStux-35" target="I8JL-FB-3HwPmqZuM6Aw-8" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-8820" y="900" as="sourcePoint" />
            <mxPoint x="-8660" y="900" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="gsT4-_EyU5koDzllndjB-159" value="1" style="endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;rounded=0;exitX=0.25;exitY=0;exitDx=0;exitDy=0;" parent="1" source="gsT4-_EyU5koDzllndjB-135" edge="1">
          <mxGeometry x="-1" y="3" relative="1" as="geometry">
            <mxPoint x="-8500" y="1070" as="sourcePoint" />
            <mxPoint x="-8670" y="840" as="targetPoint" />
            <Array as="points">
              <mxPoint x="-7820" y="1740" />
              <mxPoint x="-8670" y="1740" />
              <mxPoint x="-8670" y="840" />
            </Array>
          </mxGeometry>
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
