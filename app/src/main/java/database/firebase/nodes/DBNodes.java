package database.firebase.nodes;

public class DBNodes {
    public static final String user = "user";

    public static final class User {
        public static final String id = "user/id";
        public static final String name = "user/username";
        public static final String image = "user/image";
        public static final String email = "user/email";

        public static final class Accounts {
            public static final String id = "user/account/account_id";
            public static final String name = "user.account.account_name";

            public static String name(String id) {
                return "user/" + User.id + "/" + id + "/account_id";
            }

            public static final class Records {

            }


        }

        public static final class MainCategories {

            public static final class SubCategories {

            }

        }

        public static final class Friends {

        }

        public static final class Messages {

        }

        public static final class Notification {

        }
    }

}
