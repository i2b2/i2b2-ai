//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.09.15 at 01:59:06 PM EDT 
//


package edu.harvard.i2b2.ai.datavo.wdo;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import edu.harvard.i2b2.ai.datavo.ontology.ConceptsType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.harvard.i2b2.ai.datavo.wdo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetDblookup_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "get_dblookup");
    private final static QName _GetQuestion_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "get_question");
    private final static QName _MoveChild_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "move_child");
    private final static QName _AddChild_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "add_child");
    private final static QName _GetFoldersByUserId_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "get_folders_by_userId");
    private final static QName _GetAllDblookups_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "get_all_dblookups");
    private final static QName _GetNameInfo_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "get_name_info");
    private final static QName _RequestXML_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "requestXML");
    private final static QName _Dblookups_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "dblookups");
    private final static QName _SetProtectedAccess_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "set_protected_access");
    private final static QName _SetDblookup_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "set_dblookup");
    private final static QName _DeleteDblookup_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "delete_dblookup");
    private final static QName _AnnotateChild_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "annotate_child");
    private final static QName _RenameChild_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "rename_child");
    private final static QName _GetChildren_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "get_children");
    private final static QName _Folders_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "folders");
    private final static QName _DeleteChild_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "delete_child");
    private final static QName _ExportChild_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "export_child");
    private final static QName _GetFoldersByProject_QNAME = new QName("http://www.i2b2.org/xsd/cell/ai/1.1/", "get_folders_by_project");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.harvard.i2b2.ai.datavo.wdo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AnnotateChildType }
     * 
     */
    public AnnotateChildType createAnnotateChildType() {
        return new AnnotateChildType();
    }
    
    public ConceptsType createConceptsType() {
    	return new ConceptsType();
    }

    /**
     * Create an instance of {@link ExportChildType }
     * 
     */
    public ExportChildType createExportChildType() {
        return new ExportChildType();
    }

    /**
     * Create an instance of {@link GetChildrenType }
     * 
     */
    public GetChildrenType createGetChildrenType() {
        return new GetChildrenType();
    }

   
    /**
     * Create an instance of {@link GetReturnType }
     * 
     */
    public GetReturnType createGetReturnType() {
        return new GetReturnType();
    }

    /**
     * Create an instance of {@link ProtectedType }
     * 
     */
    public ProtectedType createProtectedType() {
        return new ProtectedType();
    }

    /**
     * Create an instance of {@link DeleteDblookupType }
     * 
     */
    public DeleteDblookupType createDeleteDblookupType() {
        return new DeleteDblookupType();
    }

  
    /**
     * Create an instance of {@link GetQuestionType }
     * 
     */
    public GetQuestionType createGetQuestionType() {
        return new GetQuestionType();
    }

    /**
     * Create an instance of {@link GetDblookupsType }
     * 
     */
    public GetDblookupsType createGetDblookupsType() {
        return new GetDblookupsType();
    }

    /**
     * Create an instance of {@link FindByChildType }
     * 
     */
    public FindByChildType createFindByChildType() {
        return new FindByChildType();
    }

    /**
     * Create an instance of {@link DeleteChildType }
     * 
     */
    public DeleteChildType createDeleteChildType() {
        return new DeleteChildType();
    }

    /**
     * Create an instance of {@link DblookupsType }
     * 
     */
    public DblookupsType createDblookupsType() {
        return new DblookupsType();
    }

    /**
     * Create an instance of {@link RenameChildType }
     * 
     */
    public RenameChildType createRenameChildType() {
        return new RenameChildType();
    }

    /**
     * Create an instance of {@link GetDblookupType }
     * 
     */
    public GetDblookupType createGetDblookupType() {
        return new GetDblookupType();
    }

    /**
     * Create an instance of {@link SetDblookupType }
     * 
     */
    public SetDblookupType createSetDblookupType() {
        return new SetDblookupType();
    }

    /**
     * Create an instance of {@link ChildType }
     * 
     */
    public ChildType createChildType() {
        return new ChildType();
    }

    /**
     * Create an instance of {@link XmlValueType }
     * 
     */
    public XmlValueType createXmlValueType() {
        return new XmlValueType();
    }

    /**
     * Create an instance of {@link MatchStrType }
     * 
     */
    public MatchStrType createMatchStrType() {
        return new MatchStrType();
    }

    /**
     * Create an instance of {@link DblookupType }
     * 
     */
    public DblookupType createDblookupType() {
        return new DblookupType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDblookupType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "get_dblookup")
    public JAXBElement<GetDblookupType> createGetDblookup(GetDblookupType value) {
        return new JAXBElement<GetDblookupType>(_GetDblookup_QNAME, GetDblookupType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQuestionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "get_question")
    public JAXBElement<GetQuestionType> createGetQuestion(GetQuestionType value) {
        return new JAXBElement<GetQuestionType>(_GetQuestion_QNAME, GetQuestionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChildType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "move_child")
    public JAXBElement<ChildType> createMoveChild(ChildType value) {
        return new JAXBElement<ChildType>(_MoveChild_QNAME, ChildType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReturnType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "get_folders_by_userId")
    public JAXBElement<GetReturnType> createGetFoldersByUserId(GetReturnType value) {
        return new JAXBElement<GetReturnType>(_GetFoldersByUserId_QNAME, GetReturnType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDblookupsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "get_all_dblookups")
    public JAXBElement<GetDblookupsType> createGetAllDblookups(GetDblookupsType value) {
        return new JAXBElement<GetDblookupsType>(_GetAllDblookups_QNAME, GetDblookupsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByChildType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "get_name_info")
    public JAXBElement<FindByChildType> createGetNameInfo(FindByChildType value) {
        return new JAXBElement<FindByChildType>(_GetNameInfo_QNAME, FindByChildType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "requestXML")
    public JAXBElement<String> createRequestXML(String value) {
        return new JAXBElement<String>(_RequestXML_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DblookupsType }{@code >}}
     * 
    
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "dblookups")
    public JAXBElement<ConceptsType> createConcepts(ConceptsType value) {
        return new JAXBElement<ConceptsType>(_Concepts_QNAME, ConceptsType.class, null, value);
    }
*/
    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DblookupsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "dblookups")
    public JAXBElement<DblookupsType> createDblookups(DblookupsType value) {
        return new JAXBElement<DblookupsType>(_Dblookups_QNAME, DblookupsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProtectedType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "set_protected_access")
    public JAXBElement<ProtectedType> createSetProtectedAccess(ProtectedType value) {
        return new JAXBElement<ProtectedType>(_SetProtectedAccess_QNAME, ProtectedType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetDblookupType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "set_dblookup")
    public JAXBElement<SetDblookupType> createSetDblookup(SetDblookupType value) {
        return new JAXBElement<SetDblookupType>(_SetDblookup_QNAME, SetDblookupType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteDblookupType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "delete_dblookup")
    public JAXBElement<DeleteDblookupType> createDeleteDblookup(DeleteDblookupType value) {
        return new JAXBElement<DeleteDblookupType>(_DeleteDblookup_QNAME, DeleteDblookupType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnnotateChildType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "annotate_child")
    public JAXBElement<AnnotateChildType> createAnnotateChild(AnnotateChildType value) {
        return new JAXBElement<AnnotateChildType>(_AnnotateChild_QNAME, AnnotateChildType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RenameChildType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "rename_child")
    public JAXBElement<RenameChildType> createRenameChild(RenameChildType value) {
        return new JAXBElement<RenameChildType>(_RenameChild_QNAME, RenameChildType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetChildrenType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "get_children")
    public JAXBElement<GetChildrenType> createGetChildren(GetChildrenType value) {
        return new JAXBElement<GetChildrenType>(_GetChildren_QNAME, GetChildrenType.class, null, value);
    }

   

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteChildType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "delete_child")
    public JAXBElement<DeleteChildType> createDeleteChild(DeleteChildType value) {
        return new JAXBElement<DeleteChildType>(_DeleteChild_QNAME, DeleteChildType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportChildType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "export_child")
    public JAXBElement<ExportChildType> createExportChild(ExportChildType value) {
        return new JAXBElement<ExportChildType>(_ExportChild_QNAME, ExportChildType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReturnType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.i2b2.org/xsd/cell/ai/1.1/", name = "get_folders_by_project")
    public JAXBElement<GetReturnType> createGetFoldersByProject(GetReturnType value) {
        return new JAXBElement<GetReturnType>(_GetFoldersByProject_QNAME, GetReturnType.class, null, value);
    }

}
