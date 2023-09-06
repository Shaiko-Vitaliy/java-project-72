package hexlet.code.model.query.assoc;

import hexlet.code.model.UrlCheck;
import hexlet.code.model.query.QUrlCheck;
import io.ebean.Transaction;
import io.ebean.typequery.Generated;
import io.ebean.typequery.PInstant;
import io.ebean.typequery.PInteger;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PString;
import io.ebean.typequery.TQAssocBean;
import io.ebean.typequery.TQProperty;
import io.ebean.typequery.TypeQueryBean;

/**
 * Association query bean for AssocUrlCheck.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@Generated("io.ebean.querybean.generator")
@TypeQueryBean("v1")
public final class QAssocUrlCheck<R> extends TQAssocBean<UrlCheck,R,QUrlCheck> {

  public PLong<R> id;
  public PInteger<R> statusCode;
  public PInstant<R> createdAt;
  public PString<R> title;
  public PString<R> h1;
  public PString<R> description;
  public QAssocUrlModel<R> url;

  public QAssocUrlCheck(String name, R root) {
    super(name, root);
  }

  public QAssocUrlCheck(String name, R root, String prefix) {
    super(name, root, prefix);
  }
}
