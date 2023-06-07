import numpy as np
from sklearn.datasets import load_iris
from sklearn.tree import DecisionTreeClassifier, plot_tree
from sklearn.inspection import DecisionBoundaryDisplay
from sklearn.metrics import confusion_matrix, classification_report
import matplotlib.pyplot as plt
import seaborn as sns

# Wczytujemy zbiór danych irysów
iris = load_iris()

# Parametry
n_classes = 3
plot_colors = "ryg"     # kolory kropek na wykresie
plot_step = 0.02        # gęstość kropków

# Przechodzimy przez listę zawierającą pary indeksów cech irysów
for pairidx, pair in enumerate([[0, 1], [0, 2], [0, 3], [1, 2], [1, 3], [2, 3]]):
    # Cechy (X) i etykiety klas (y) dla każdego przykładu w zbiorze
    X = iris.data[:, pair]
    y = iris.target

    # Inicjalizujemy klasyfikator drzewa decyzyjnego i trenujemy go na danych X i y.
    # fit() dopasowuje model do danych treningowych, ucząc go,
    # jak przewidywać etykiety klas na podstawie dostarczonych cech
    clf = DecisionTreeClassifier().fit(X, y)

    # Granica decyzyjna
    ax = plt.subplot(2, 3, pairidx + 1)
    plt.tight_layout(h_pad=0.5, w_pad=0.5, pad=2.5)
    DecisionBoundaryDisplay.from_estimator(
        clf,
        X,
        cmap=plt.cm.Blues,                    # ustawiłam na niebieskie kolorki ale mozna popatrzec na inne
        response_method="predict",
        ax=ax,
        xlabel=iris.feature_names[pair[0]],
        ylabel=iris.feature_names[pair[1]],
    )

    # Trenowane punkty
    for i, color in zip(range(n_classes), plot_colors):
        idx = np.where(y == i)
        plt.scatter(
            X[idx, 0],
            X[idx, 1],
            c=color,
            label=iris.target_names[i],
            cmap=plt.cm.Blues,
            edgecolor="black",
            s=15,
        )


# Wyświetlamy diagram drzewa decyzyjnego
plt.figure()
clf = DecisionTreeClassifier().fit(iris.data, iris.target)
plot_tree(clf, filled=True)
plt.title("Drzewo decyzyjne trenowane na wszystkich cechach irysów")
plt.show()

# Macierz pomyłek w formie terminalowej i graficznej
predicted_labels = clf.predict(iris.data) # przewidziane etykiety klas na podstawie danych treningowych
confusion_mat = confusion_matrix(iris.target, predicted_labels)
print("Macierz pomyłek:")
print(confusion_mat)

labels = iris.target_names
sns.heatmap(confusion_mat, annot=True, xticklabels=labels, yticklabels=labels)
plt.title("Macierz pomyłek")
plt.xlabel('Przewidziane etykiety')
plt.ylabel('Rzeczywiste etykiety')
plt.show()

# Self-explanatory, wyświetlane terminalowo
classification_rep = classification_report(iris.target, predicted_labels, target_names=iris.target_names)
print("\nWartości precision, recall i F-measure:")
print(classification_rep)

# Kolejny wykresik który się rozkleja obecnie
plt.suptitle("Powierzchnia decyzyjna drzew decyzyjnych trenowanych na parach cech")
plt.legend(loc="lower right", borderpad=0, handletextpad=0)
_ = plt.axis("tight")
plt.show()



# iris = load_iris()  # wczytujemy zbiór danych irysów
# X, y = iris.data, iris.target  # cechy (X) i etykiety klas (y) dla każdego przykładu w zbiorze
#
# # Inicjalizujemy klasyfikator drzewa decyzyjnego
# clf = tree.DecisionTreeClassifier()
# # Trenujemy klasyfikator drzewa decyzyjnego na danych X i y.
# # Metoda fit() dopasowuje model do danych treningowych,
# # ucząc go, jak przewidywać etykiety klas na podstawie dostarczonych cech
# clf = clf.fit(X, y)
#
# # Wyświetlamy diagram drzewa decyzyjnego
# tree.plot_tree(clf)
# plt.show()
#
# # Analizujemy wyniki trenowania
# predicted_labels = clf.predict(X)  # przewidziane etykiety klas na podstawie danych treningowych
# accuracy = clf.score(X, y)  # dokładność klasyfikatora na danych treningowych
#
# # Wyświetlamy macierz pomyłek
# confusion_mat = confusion_matrix(y, predicted_labels)
# print("Macierz pomyłek:")
# print(confusion_mat)
#
# # Wyświetlamy macierz pomyłek w postaci graficznej
# labels = iris.target_names
# sns.heatmap(confusion_mat, annot=True, xticklabels=labels, yticklabels=labels)
# plt.xlabel('Przewidziane etykiety')
# plt.ylabel('Rzeczywiste etykiety')
# plt.show()
#
# print("Przewidziane etykiety klas:", predicted_labels)
# print("Dokładność klasyfikatora:", accuracy)


