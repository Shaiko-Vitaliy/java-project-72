package hexlet.code.model.query;

import hexlet.code.model.UrlModel;
import hexlet.code.model.query.assoc.QAssocUrlCheck;
import io.ebean.Database;
import io.ebean.FetchGroup;
import io.ebean.Query;
import io.ebean.Transaction;
import io.ebean.typequery.Generated;
import io.ebean.typequery.PInstant;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PString;
import io.ebean.typequery.TQRootBean;
import io.ebean.typequery.TypeQueryBean;

/**
 * Query bean for UrlModel.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@Generated("io.ebean.querybean.generator")
@TypeQueryBean("v1")
public final class QUrlModel extends TQRootBean<UrlModel,QUrlModel> {

  private static final QUrlModel _alias = new QUrlModel(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QUrlModel alias() {
    return _alias;
  }

  public PLong<QUrlModel> id;
  public PString<QUrlModel> url;
  public PInstant<QUrlModel> createdAt;
  public QAssocUrlCheck<QUrlModel> urlChecks;


  /**
   * Return a query bean used to build a FetchGroup.
   * <p>
   * FetchGroups are immutable and threadsafe and can be used by many
   * concurrent queries. We typically stored FetchGroup as a static final field.
   * <p>
   * Example creating and using a FetchGroup.
   * <pre>{@code
   * 
   * static final FetchGroup<Customer> fetchGroup = 
   *   QCustomer.forFetchGroup()
   *     .shippingAddress.fetch()
   *     .contacts.fetch()
   *     .buildFetchGroup();
   * 
   * List<Customer> customers = new QCustomer()
   *   .select(fetchGroup)
   *   .findList();
   * 
   * }</pre>
   */
  public static QUrlModel forFetchGroup() {
    return new QUrlModel(FetchGroup.queryFor(UrlModel.class));
  }

  /**
   * Construct using the default Database.
   */
  public QUrlModel() {
    super(UrlModel.class);
  }

  /**
   * Construct with a given transaction.
   */
  public QUrlModel(Transaction transaction) {
    super(UrlModel.class, transaction);
  }

  /**
   * Construct with a given Database.
   */
  public QUrlModel(Database database) {
    super(UrlModel.class, database);
  }


  /**
   * Construct for Alias.
   */
  private QUrlModel(boolean dummy) {
    super(dummy);
  }

  /**
   * Private constructor for FetchGroup building.
   */
  private QUrlModel(Query<UrlModel> fetchGroupQuery) {
    super(fetchGroupQuery);
  }

  /**
   * Provides static properties to use in <em> select() and fetch() </em>
   * clauses of a query. Typically referenced via static imports. 
   */
  public static class Alias {
    public static PLong<QUrlModel> id = _alias.id;
    public static PString<QUrlModel> url = _alias.url;
    public static PInstant<QUrlModel> createdAt = _alias.createdAt;
    public static QAssocUrlCheck<QUrlModel> urlChecks = _alias.urlChecks;
  }
}
